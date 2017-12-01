package in.nj.nearby.services;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by hp on 22-11-2017.
 */

public class DemoRequest {

    public enum TYPE{
        GET(Request.Method.GET),
        POST(Request.Method.GET),
        PUT(Request.Method.GET),
        DELETE(Request.Method.GET);

        private final int value;

        TYPE(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }
    }

    private TYPE type;
    private String url;
    private Map<String,String> parameters;
    private Map<String,String> headers;
    private JSONObject jsonObject;
    private Request.Priority priority;

    private DemoRequest(Builder builder) {
        this.type = builder.type;
        this.url = builder.url;
        this.parameters = builder.parameters;
        this.headers = builder.headers;
        this.jsonObject = builder.jsonObject;
        this.priority = builder.priority;
    }

    public static TypeStep builder(){
        return new Builder();
    }

    public interface TypeStep{
        URLStep requestMethod(TYPE type);
    }

    public interface URLStep{
        HeadersStep url(String url);
    }

    public interface HeadersStep{
        Build headers(Map<String, String> headers);
    }

    public interface Build{
        DemoRequest build();
        Build parameters(Map<String, String> parameters);
        Build jsonObject(JSONObject object);
        Build priority(Request.Priority priority);
    }

    public static class Builder implements TypeStep,URLStep,HeadersStep,Build{

        private TYPE type;
        private String url;
        private Map<String,String> parameters;
        private Map<String,String> headers;
        private JSONObject jsonObject;
        private Request.Priority priority = Request.Priority.NORMAL;

        @Override
        public URLStep requestMethod(TYPE type) {
            this.type = type;
            return this;
        }

        @Override
        public HeadersStep url(String url) {
            this.url = url;
            return this;
        }

        @Override
        public Build headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public DemoRequest build() {
            return new DemoRequest(this);
        }

        @Override
        public Build parameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        @Override
        public Build jsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }

        @Override
        public Build priority(Request.Priority priority) {
            this.priority = priority;
            return this;
        }
    }

    public TYPE getType() {
        return type;
    }

    public String getUrl(){
        return url;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public Request.Priority getPriority() { return priority; }
}
