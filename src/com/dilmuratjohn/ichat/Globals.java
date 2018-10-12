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

        COMMAND{
            public String toString() {
                return "/";
            }
        },

        END_ALL {
            public String toString() {
                return "/end all/";
            }
        },

        SHOW_CLIENTS {
            public String toString() {
                return "/show clients/";
            }
        },

        TOGGLE_RAW_MODE {
            public String toString() {
                return "/toggle raw mode/";
            }
        },

        SEND_MESSAGE_TO_ALL {
            public String toString() {
                return "/send message to all/";
            }
        },

        KICK {
            public String toString() {
                return "/kick/";
            }
        }
    }
}
