package com.batsoft.utils;

import java.math.BigDecimal;

/**
 * @author:         shangxl
 * @Date :          2017年3月13日 下午7:38:15
 */
public final class Convert {
    private Convert() { }

    /**
     * 小单位转化到大单位
     * @author:         shangxl
     * @param:    @param number
     * @param:    @param unit
     * @param:    @return
     * @return: BigDecimal 
     * @Date :          2018年3月29日 上午10:32:48   
     * @throws:
     */
    public static BigDecimal fromWei(String number, Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }
    
    /**
     * 小单位转化到大单位
     * @author:         shangxl
     * @param:    @param number
     * @param:    @param unit
     * @param:    @return
     * @return: BigDecimal 
     * @Date :          2018年3月29日 下午1:54:30   
     * @throws:
     */
    public static BigDecimal fromWei(BigDecimal number, Unit unit) {
        return number.divide(unit.getWeiFactor());
    }

    /**
     * 由大单位转化到小单位
     * @author:         shangxl
     * @param:    @param number
     * @param:    @param unit
     * @param:    @return
     * @return: BigDecimal 
     * @Date :          2018年3月29日 上午10:34:06   
     * @throws:
     */
    public static BigDecimal toWei(String number, Unit unit) {
        return toWei(new BigDecimal(number), unit);
    }

    /**
     * 
     * <p> 由大单位转化到小单位</p>
     * @author:         shangxl
     * @param:    @param number
     * @param:    @param unit
     * @param:    @return
     * @return: BigDecimal
     * @Date :          2018年3月29日 下午1:54:14
     * @throws:
     */
    public static BigDecimal toWei(BigDecimal number, Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public enum Unit {

    	//精度0位
    	WEI("wei", 0),
    	//精度0位
        KWEI("kwei", 3),
        //精度0位
        MWEI("mwei", 6),
        //精度0位
        GWEI("gwei", 9),
        //精度0位
        SZABO("szabo", 12),
        //精度0位
        FINNEY("finney", 15),
        //精度0位
        ETHER("ether", 18),
        TETHER("Tether", 6),
        //精度0位
        KETHER("kether", 21),
        //精度0位
        METHER("mether", 24),
        //精度0位
        GETHER("gether", 27),
        //精度0位
    	ONE("one",1),
    	//精度0位
    	TWO("two",2),
    	//精度0位
    	FOUR("four",4),
    	//精度0位
    	FIVER("fiver",5),
    	//精度0位
    	SEVEN("seven",7),
    	//精度0位
    	EIGHT("eight",8),
    	//精度0位
    	TEN("ten",10),
    	//精度0位
    	ELEVEN("eleven",11),
    	//精度0位
    	THIRTEEN("thirteen",13);
    	
        private String name;
        private BigDecimal weiFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.weiFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getWeiFactor() {
            return weiFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}
