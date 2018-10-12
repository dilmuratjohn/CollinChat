package com.dilmuratjohn.ichat;

public enum Prefix {
    CONNECTION {
        public String toString() {
            return "/c/";
        }
    },

    MESSAGE {
        public String toString() {
            return "/m/";
        }
    },
}


