package com.pipeline;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MetricJsonUtils {

    /** 전체 CPU 사용량 퍼센티지: system -> cpu -> total -> norm -> pct <br>
     *  메트릭 종류 추출: metricset > name <br>
     *  호스트 이름과 timestamp <br>
     *    - 호스트 이름: host > name <br>
     *    - timestamp: @timestamp
     */
    public static double getTotalCpuPercent(String value) {
        return JsonParser.parseString(value)
                .getAsJsonObject().get("system")
                .getAsJsonObject().get("cpu")
                .getAsJsonObject().get("total")
                .getAsJsonObject().get("norm")
                .getAsJsonObject().get("pct")
                .getAsDouble();
    }

    public static String getMetricName(String value) {
        return JsonParser.parseString(value)
                .getAsJsonObject().get("metricset")
                .getAsJsonObject().get("name")
                .getAsString();
    }

    public static String getHostTimestamp(String value) {
        JsonObject objectValue = JsonParser.parseString(value).getAsJsonObject();
        JsonObject result = objectValue.getAsJsonObject("host");
        result.add("timestamp", objectValue.get("@timestamp"));

        return result.toString();
    }

}
