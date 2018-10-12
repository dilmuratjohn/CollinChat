package com.dilmuratjohn.ichat;

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


