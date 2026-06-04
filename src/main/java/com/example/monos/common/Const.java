package com.example.monos.common;

public class Const {
    public static final String URL = "http://localhost:8080";
    
    /* 処理結果メッセージ */
    public static final String MESSAGE_TYPE_SUCCESS = "SUCCESS";
    public static final String MESSAGE_TYPE_WRAN = "WRAN";
    public static final String MESSAGE_TYPE_ERROR = "ERROR";
    
    /* roles */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GENERAL = "ROLE_GENERAL";
    public static final String ROLE_REFERENCE = "ROLE_REFERENCE";
    
    /* codeType */
    public static final String CODE_TYPE_INBOUND_STATUS = "INBOUND_STATUS";
    public static final String CODE_TYPE_OUTBOUND_STATUS = "OUTBOUND_STATUS";
    
    /* 入庫状態 */
    public static final class InboundStatus{
        public static final int MINYUKO = 0;
        public static final String MINYUKO_LABEL = "未入庫";
        
        public static final int NYUKOCHU = 1;
        public static final String NYUKOCHU_LABEL = "入庫中";
        
        public static final int NYUKOZUMI = 2;
        public static final String NYUKOZUMI_LABEL = "入庫済";
        
        public static final int CANCEL = 3;
        public static final String CANCEL_LABEL = "キャンセル";
    }

    /* 出庫状態  */
    public static final class OutboundStatus{
        public static final int MINSHUKO = 0;
        public static final String MINSHUKO_LABEL = "未出庫";
        
        public static final int SHUKOCHU = 1;
        public static final String SHUKOCHU_LABEL = "出庫中";
        
        public static final int SHUKOZUMI = 2;
        public static final String SHUKOZUMI_LABEL = "出庫済";
        
        public static final int CANCEL = 3;
        public static final String CANCEL_LABEL = "キャンセル";
    }
}
