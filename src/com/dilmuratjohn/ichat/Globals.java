package com.dilmuratjohn.ichat;

public class Globals {

    public enum Prefix {

        CONNECTION {
            public String toString() {
                return "/c/";
            }
        },

        DISCONNECTION {
            public String toString() {
                return "/d/";
            }
        },

        MESSAGE {
            public String toString() {
                return "/m/";
            }
        },

        PING {
            public String toString() {
                return "/p/";
            }
        },

        KICKED {
            public String toString() {
                return "/k/";
            }
        },

        ONLINE_USER {
            public String toString() {
                return "/u/";
            }
        },
    }

    public enum Status {

        LEFT {
            public String toString() {
                return "left";
            }
        },

        TIMEOUT {
            public String toString() {
                return "timeout";
            }
        },

        KICKED {
            public String toString() {
                return "kicked";
            }
        },
    }

    public enum Command {

        HELP{
            public String toString(){
                return "/help/";
            }
        },

        QUIT {
            public String toString() {
                return "/quit/";
            }
        },

        SHOW_CLIENTS {
            public String toString() {
                return "/clients/";
            }
        },

        TOGGLE_RAW_MODE {
            public String toString() {
                return "/raw/";
            }
        },

        SEND_MESSAGE_TO_ALL {
            public String toString() {
                return "/send/";
            }
        },

        KICK {
            public String toString() {
                return "/kick/";
            }
        }
    }
}
