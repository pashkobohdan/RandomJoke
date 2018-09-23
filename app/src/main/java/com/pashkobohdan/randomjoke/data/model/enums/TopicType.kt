package com.pashkobohdan.randomjoke.data.model.enums

enum class TopicType(val code: Int) {
    ALL(0),

    ANECDOT(1),
    RASSKAZ(2),
    STISHKI(3),
    AFORIZMI(4),
    CITATI(5),
    TOSTY(6),
    STATUSY(8),
    ANECDOT_18(11),
    RASSKAZ_18(12),
    STISHKI_18(13),
    AFORIZMI_18(14),
    CITATI_18(15),
    TOSTY_18(16),
    STATUSY_18(18);

    companion object {
        fun findByCode(code: Int) : TopicType {
            for(value in values()) {
                if(value.code.equals(code)) {
                    return value
                }
            }
            throw IllegalArgumentException("There's no topic type with this code ${code}")
        }
    }
}