package com.example.intentsupportapp.dto;

import com.example.intentsupportapp.enums.IntentExtraKind;

public class IntentExtraDto {

    public IntentExtraKind extraKind;
    public String key;
    public String value;

    public IntentExtraDto(IntentExtraKind extraKind, String key, String value) {
        this.extraKind = extraKind;
        this.key = key;
        this.value = value;
    }

    public boolean equals(IntentExtraDto other) {
        if (this.extraKind.equals(other.extraKind) &&
                this.key.equals(other.key) &&
                this.value.equals(other.value)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(extraKind.getExtraCommand());
        sb.append(" ");
        sb.append(key);
        sb.append(" ");
        sb.append(value);

        return sb.toString();
    }

}
