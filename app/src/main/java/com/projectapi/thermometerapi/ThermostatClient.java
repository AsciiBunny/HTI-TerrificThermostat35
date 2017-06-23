package com.projectapi.thermometerapi;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by s161047 on 19-6-2017.
 */

public class ThermostatClient {
    protected static final String BASE_URL = "http://wwwis.win.tue.nl/2id40-ws/";
    protected static String GROUP_NUMBER;
    protected static final double EPSILON = 0.01;
    protected static final int NR_SWITCHES_WEEK = 10;
    protected static final DecimalFormat TEMP_FORMAT = new DecimalFormat("##.0");

    private static ThermostatData thermostatData = new ThermostatData();

    public static void init(int groupNumber, ThermostatDataHandler thermostatDataHandler) {
        GROUP_NUMBER = groupNumber + "";
        loadDataFromServer(thermostatDataHandler);
    }

    private static void loadDataFromServer(final ThermostatDataHandler thermostatDataHandler) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = getURL("");
                    URLConnection conn = url.openConnection();

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(conn.getInputStream());

                    thermostatData.weekDay = getWeekdayFromDoc(doc);
                    thermostatData.time = getTimeFromDoc(doc);
                    thermostatData.currentTemperature = getCurrentTemperatureFromDoc(doc);
                    thermostatData.targetTemperature = getTargetTemperatureFromDoc(doc);
                    thermostatData.weekProgram.dayTemperature = getDayTemperatureFromDoc(doc);
                    thermostatData.weekProgram.nightTemperature = getNightTemperatureFromDoc(doc);
                    thermostatData.weekProgramState = getWeekStateProgramFromDoc(doc);
                    thermostatData.weekProgram.weekDaySwitchMap = getWeekDaySwitches(doc);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (thermostatDataHandler != null) {
                    thermostatDataHandler.onFinish(thermostatData);
                }

                return null;
            }
        }.execute();
    }

    private static WeekDay getWeekdayFromDoc(Document doc) {
        return WeekDay.parseWeekDay(doc.getElementsByTagName("current_day").item(0).getTextContent());
    }

    private static double getCurrentTemperatureFromDoc(Document doc) {
        return Double.parseDouble(doc.getElementsByTagName("current_temperature").item(0).getTextContent());
    }

    private static double getTargetTemperatureFromDoc(Document doc) {
        return Double.parseDouble(doc.getElementsByTagName("target_temperature").item(0).getTextContent());
    }

    private static double getDayTemperatureFromDoc(Document doc) {
        return Double.parseDouble(doc.getElementsByTagName("day_temperature").item(0).getTextContent());
    }

    private static double getNightTemperatureFromDoc(Document doc) {
        return Double.parseDouble(doc.getElementsByTagName("night_temperature").item(0).getTextContent());
    }

    private static boolean getWeekStateProgramFromDoc(Document doc) {
        return doc.getElementsByTagName("week_program_state").item(0).getTextContent().equals("on");
    }

    private static String getTimeFromDoc(Document doc) {
        return doc.getElementsByTagName("time").item(0).getTextContent();
    }

    private static Map<WeekDay, Switch[]> getWeekDaySwitches(Document doc) {
        Map<WeekDay, Switch[]> weekDaySwitchMap = new HashMap<>();

        Element weekProgramParentNode = (Element) doc.getElementsByTagName("week_program").item(0);

        for (int i = 0; i < weekProgramParentNode.getElementsByTagName("day").getLength(); i++) {

            Element weekDayProgramNode = (Element) weekProgramParentNode.getElementsByTagName("day").item(i);
            WeekDay weekDay = WeekDay.parseWeekDay(weekDayProgramNode.getAttributes().getNamedItem("name").getNodeValue());
            Switch[] switches = new Switch[NR_SWITCHES_WEEK];

            for (int j = 0; j < weekDayProgramNode.getElementsByTagName("switch").getLength(); j++) {
                Element switchNode = (Element) weekDayProgramNode.getElementsByTagName("switch").item(j);

                String time = switchNode.getTextContent();
                SwitchType type = switchNode.getAttributes().getNamedItem("type").getNodeValue().equals("Day") ? SwitchType.DAY : SwitchType.NIGHT;
                boolean state = switchNode.getAttributes().getNamedItem("state").getNodeValue().equals("on");

                Switch theSwitch = new Switch(type, state, time);
                switches[j] = theSwitch;
            }

            weekDaySwitchMap.put(weekDay, switches);
        }

        return weekDaySwitchMap;

    }

    private interface DataHandler {
        void handle(Document doc);
    }

    private static void readURL(final URL url, final DataHandler dataHandler) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Document doc = null;

                try {
                    URLConnection conn = url.openConnection();

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    doc = builder.parse(conn.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dataHandler.handle(doc);
                return null;
            }
        }.execute();
    }

    private static void putURL(final URL url, final String data) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestMethod("PUT");
                    httpCon.setRequestProperty("Content-Type", "application/xml");
                    //OutputStreamWriter out = new OutputStreamWriter(
                    //        httpCon.getOutputStream());
                    DataOutputStream out = new DataOutputStream(httpCon.getOutputStream());
                    out.writeBytes(data);
                    out.flush();

                    if (httpCon.getResponseCode() != 200) {
                        InputStream err = httpCon.getErrorStream();
                        BufferedReader err_read = new BufferedReader(
                                new InputStreamReader(err));
                        String errInput;
                        while ((errInput = err_read.readLine()) != null) {
                            Log.e("Testing", "ErrorStream: " + errInput);
                        }
                        err.close(); // Close the Error Stream.
                        err_read.close();
                    }

                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public static String parseTempToString(double x) {
        return TEMP_FORMAT.format(x);
    }

    private static void putWeekDay() {
        String XMLContent = createXMLWithOneTag("current_day", thermostatData.getWeekDay().name);
        putURL(getURL("day"), XMLContent);
    }

    private static void putTime() {
        String XMLContent = createXMLWithOneTag("time", thermostatData.getTime());
        putURL(getURL("time"), XMLContent);
    }

    private static void putCurrentTemperature() {
        String XMLContent = createXMLWithOneTag("current_temperature", parseTempToString(thermostatData.getCurrentTemperature()));
        putURL(getURL("currentTemperature"), XMLContent);
    }

    private static void putTargetTemperature() {
        String XMLContent = createXMLWithOneTag("target_temperature", parseTempToString(thermostatData.getTargetTemperature()));
        putURL(getURL("targetTemperature"), XMLContent);
    }

    private static void putDayTemperature() {
        String XMLContent = createXMLWithOneTag("day_temperature", parseTempToString(thermostatData.getDayTemperature()));
        putURL(getURL("dayTemperature"), XMLContent);
    }

    private static void putNightTemperature() {
        String XMLContent = createXMLWithOneTag("night_temperature", parseTempToString(thermostatData.getNightTemperature()));
        putURL(getURL("nightTemperature"), XMLContent);
    }

    private static void putWeekProgramState() {
        String XMLContent = createXMLWithOneTag("week_program_state", thermostatData.getWeekProgramState() ? "on" : "off");
        putURL(getURL("weekProgramState"), XMLContent);
    }

    private static String createXMLWithOneTag(String tagName, String value) {
        return "<" + tagName + ">" + value + "</" + tagName + ">";
    }

    private static void checkInitialized() {
        if (thermostatData == null) {
            throw new IllegalStateException("You must initialize the ThermostatClient first");
        }
    }

    public static ThermostatData getThermostatData() {
        checkInitialized();

        return thermostatData;
    }

    public static void updateThermometerData(final ThermostatDataHandler handler) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                if (thermostatData == null) {
                    return null;
                }

                if (thermostatData.isWeekDayDirty) {
                    putWeekDay();
                    thermostatData.isWeekDayDirty = false;
                }

                if (thermostatData.isTimeDirty) {
                    putTime();
                    thermostatData.isTimeDirty = false;
                }

                if (thermostatData.isCurrentTemperatureDirty) {
                    putCurrentTemperature();
                    thermostatData.isCurrentTemperatureDirty = false;
                }

                if (thermostatData.isTargetTemperatureDirty) {
                    putTargetTemperature();
                    thermostatData.isTargetTemperatureDirty = false;
                }

                if (thermostatData.isDayTemperatureDirty) {
                    putDayTemperature();
                    thermostatData.isDayTemperatureDirty = false;
                }

                if (thermostatData.isNightTemperatureDirty) {
                    putNightTemperature();
                    thermostatData.isNightTemperatureDirty = false;
                }

                if (thermostatData.isWeekProgramStateDirty) {
                    putWeekProgramState();
                    thermostatData.isWeekProgramStateDirty = false;
                }

//                for (Map.Entry<WeekDay, Boolean> entry : thermostatData.weekDayDirtySwitchMap.entrySet()) {
//                    if (entry.getValue()) {
//                        update
//                    }
//                }

                loadDataFromServer(new ThermostatDataHandler() {
                    @Override
                    public void onFinish(ThermostatData thermostatData) {
                        if (handler != null) {
                            handler.onFinish(thermostatData);
                        }
                    }
                });

                return null;
            }
        }.execute();
    }

    private static URL getURL(String pathName) {
        try {
            return new URL(BASE_URL + GROUP_NUMBER + "/" + pathName);
        } catch (MalformedURLException error) {
            // if this happens it is probably a programmer error
            error.printStackTrace();
        }

        return null;
    }
}
