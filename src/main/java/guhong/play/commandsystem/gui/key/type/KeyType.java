package guhong.play.commandsystem.gui.key.type;

import lombok.Data;

/**
 * key类型
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public enum  KeyType{
    /**
     * 会打印值的按键
     */
    PRINT(1),
    /**
     * 不会打印值的按键
     */
    NOT_PRINT(2);


    private Integer code;





    KeyType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}

