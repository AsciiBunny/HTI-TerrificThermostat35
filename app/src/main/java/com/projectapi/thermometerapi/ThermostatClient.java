package com.projectapi.thermometerapi;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by s161047 on 19-6-2017.
 */

public class ThermostatClient {
    protected static final String BASE_URL = "http://wwwis.win.tue.nl/2id40-ws/";
    protected static String GROUP_NUMBER;
    protected static final double EPSILON = 0.01;
    public static final int NR_SWITCHES_WEEK = 10;
    protected static final DecimalFormat TEMP_FORMAT = new DecimalFormat("##.0");

    private static ThermostatData thermostatData = new ThermostatData();

    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;

    public static void init(int groupNumber, ThermostatDataHandler thermostatDataHandler) {
        GROUP_NUMBER = groupNumber + "";

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadDataFromServer(thermostatDataHandler);
    }

    private static void loadDataFromServer(final ThermostatDataHandler thermostatDataHandler) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = getURL("");
                    URLConnection conn = url.openConnection();

                    Document doc = builder.parse(conn.getInputStream());

                    WeekDay newWeekDay = getWeekdayFromDoc(doc);
                    String newTime = getTimeFromDoc(doc);
                    double newCurrentTemperature = getCurrentTemperatureFromDoc(doc);
                    double newTargetTemperature = getTargetTemperatureFromDoc(doc);
                    double newDayTemperature = getDayTemperatureFromDoc(doc);
                    double newNightTemperature = getNightTemperatureFromDoc(doc);
                    boolean newWeekProgramState = getWeekStateProgramFromDoc(doc);
                    Map<WeekDay, Switch[]> newWeekDaySwitchMap = getWeekDaySwitches(doc);

                    if (thermostatData.isWeekDayDirty) {
                        if (newWeekDay == thermostatData.weekDay) {
                            thermostatData.isWeekDayDirty = false;
                            thermostatData.weekDay = newWeekDay;
                        }
                    } else {
                        thermostatData.weekDay = newWeekDay;
                    }

                    if (thermostatData.isTimeDirty) {
                        if (!newTime.equals(thermostatData.time)) {
                            thermostatData.isTimeDirty = false;
                            thermostatData.time = newTime;
                        }
                    } else {
                        thermostatData.time = newTime;
                    }

                    if (thermostatData.isCurrentTemperatureDirty) {
                        if (isTempEqual(newCurrentTemperature, thermostatData.currentTemperature)) {
                            thermostatData.isCurrentTemperatureDirty = false;
                            thermostatData.currentTemperature = newCurrentTemperature;
                        }
                    } else {
                        thermostatData.currentTemperature = newCurrentTemperature;
                    }

                    if (thermostatData.isTargetTemperatureDirty) {
                        if (isTempEqual(newTargetTemperature, thermostatData.targetTemperature)) {
                            thermostatData.isTargetTemperatureDirty = false;
                            thermostatData.targetTemperature = newTargetTemperature;
                        }
                    } else {
                        thermostatData.targetTemperature = newTargetTemperature;
                    }

                    if (thermostatData.isDayTemperatureDirty) {
                        if (isTempEqual(newDayTemperature, thermostatData.weekProgram.dayTemperature)) {
                            thermostatData.isDayTemperatureDirty = false;
                            thermostatData.weekProgram.dayTemperature = newDayTemperature;
                        }
                    } else {
                        thermostatData.weekProgram.dayTemperature = newDayTemperature;
                    }

                    if (thermostatData.isNightTemperatureDirty) {
                        if (isTempEqual(newNightTemperature, thermostatData.weekProgram.nightTemperature)) {
                            thermostatData.isNightTemperatureDirty = false;
                            thermostatData.weekProgram.nightTemperature = newNightTemperature;
                        }
                    } else {
                        thermostatData.weekProgram.nightTemperature = newNightTemperature;
                    }

                    if (thermostatData.isWeekProgramStateDirty) {
                        if (thermostatData.isWeekProgramStateDirty == newWeekProgramState) {
                            thermostatData.isWeekProgramStateDirty = false;
                            thermostatData.weekProgramState = newWeekProgramState;
                        }
                    } else {
                        thermostatData.weekProgramState = newWeekProgramState;
                    }

                    if (thermostatData.isWeekDaySwitchMapDirty) {
                        if (newWeekDaySwitchMap.equals(thermostatData.weekProgram.weekDaySwitchMap)) {
                            thermostatData.isWeekProgramStateDirty = false;
                            thermostatData.weekProgram.weekDaySwitchMap = newWeekDaySwitchMap;
                        }
                    } else {
                        thermostatData.weekProgram.weekDaySwitchMap = newWeekDaySwitchMap;
                    }

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

    private static boolean isTempEqual(double temp1, double temp2) {
        return Math.abs(temp1 - temp2) < EPSILON;
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
                SwitchType type = switchNode.getAttributes().getNamedItem("type").getNodeValue().equals("day") ? SwitchType.DAY : SwitchType.NIGHT;
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

    private static void putWeekDaySwitchMap() {
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("week_program");
        rootElement.setAttribute("state", thermostatData.weekProgramState ? "on" : "off");
        doc.appendChild(rootElement);

        for (WeekDay weekDay : WeekDay.values()) {
            Element weekdayElement = doc.createElement("day");
            weekdayElement.setAttribute("name", weekDay.name);

            for (Switch littleSwitch : thermostatData.weekProgram.weekDaySwitchMap.get(weekDay)) {
                Element littleSwitchElement = doc.createElement("switch");

                littleSwitchElement.setAttribute("type", littleSwitch.getSwitchType() == SwitchType.DAY ? "day" : "night");
                littleSwitchElement.setAttribute("state", littleSwitch.getState() ? "on" : "off");
                littleSwitchElement.setTextContent(littleSwitch.getTime());

                weekdayElement.appendChild(littleSwitchElement);
            }

            rootElement.appendChild(weekdayElement);
        }

        String XMLContent = toString(doc);
        putURL(getURL("weekProgram"), XMLContent);

    }

    public static String toString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
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

                loadDataFromServer(new ThermostatDataHandler() {
                    @Override
                    public void onFinish(ThermostatData thermostatData) {
                        if (handler != null) {
                            handler.onFinish(thermostatData);
                        }
                    }
                });

                if (thermostatData == null) {
                    return null;
                }

                if (thermostatData.isWeekDayDirty) {
                    putWeekDay();
                }

                if (thermostatData.isTimeDirty) {
                    putTime();
                }

                if (thermostatData.isCurrentTemperatureDirty) {
                    putCurrentTemperature();
                }

                if (thermostatData.isTargetTemperatureDirty) {
                    putTargetTemperature();
                }

                if (thermostatData.isDayTemperatureDirty) {
                    putDayTemperature();
                }

                if (thermostatData.isNightTemperatureDirty) {
                    putNightTemperature();
                }

                if (thermostatData.isWeekProgramStateDirty) {
                    putWeekProgramState();
                }

                if (thermostatData.isWeekDaySwitchMapDirty) {
                    putWeekDaySwitchMap();
                }

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
