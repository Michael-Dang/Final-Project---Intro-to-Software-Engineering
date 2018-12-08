package ceg4110_f18_g19.isitfood.client;


class ServerConfiguration {
    private static final ServerConfiguration ourInstance = new ServerConfiguration();

    private static String serverUrl;
    static ServerConfiguration getInstance() {
        return ourInstance;
    }
    public String getURL() { return serverUrl; }
    public void setURL(String url) { serverUrl = url; }
    private ServerConfiguration(){
    }
}
