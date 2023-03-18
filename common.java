import java.util.Collection;

class Common
{
    static void define(Scope scope) {
        scope.define("PI", (float) Math.PI);

        scope.define("degree", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.toDegrees((float) arguments[0]);
            }
        });
        scope.define("radian", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.toRadians((float) arguments[0]);
            }
        });
        scope.define("sin", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.sin((float) arguments[0]);
            }
        });
        scope.define("cos", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.cos((float) arguments[0]);
            }
        });
        scope.define("tan", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.tan((float) arguments[0]);
            }
        });
        scope.define("floor", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.floor((float) arguments[0]);
            }
        });
        scope.define("round", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.round((float) arguments[0]);
            }
        });
        scope.define("ceil", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.ceil((float) arguments[0]);
            }
        });
        scope.define("sqrt", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.sqrt((float) arguments[0]);
            }
        });
        scope.define("pow", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.pow((float) arguments[0], (float) arguments[1]);
            }
        });
        scope.define("atan2", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.atan2((float) arguments[0], (float) arguments[1]);
            }
        });
        scope.define("min", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.min((float) arguments[0], (float) arguments[1]);
            }
        });
        scope.define("max", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.max((float) arguments[0], (float) arguments[1]);
            }
        });
        scope.define("random", new Function() {
            Object call(Object[] arguments) {
                return (float) Math.random();
            }
        });
        scope.define("randomrange", new Function() {
            Object call(Object[] arguments) {
                float min = (float) arguments[0];
                float max = (float) arguments[1];
                return (float) Math.floor(Math.random() * (max - min) + min);
            }
        });
        scope.define("add", new Function() {
            Object call(Object[] arguments) {
                ((Collection) arguments[0]).add(arguments[1]);
                return null;
            }
        });
        scope.define("remove", new Function() {
            Object call(Object[] arguments) {
                ((Collection) arguments[0]).remove(arguments[1]);
                return null;
            }
        });
        scope.define("contains", new Function() {
            Object call(Object[] arguments) {
                return ((Collection) arguments[0]).contains(arguments[1]);
            }
        });
        scope.define("size", new Function() {
            Object call(Object[] arguments) {
                return ((Collection) arguments[0]).size();
            }
        });
        scope.define("time", new Function() {
            Object call(Object[] arguments) {
                return (float) (int) System.currentTimeMillis();
            }
        });
        scope.define("concat", new Function() {
            Object call(Object[] arguments) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arguments.length; i++) {
                    sb.append(arguments[i]);
                }
                return sb.toString();
            }
        });
        scope.define("chr", new Function() {
            Object call(Object[] arguments) {
                return String.valueOf((char) (int) (float) arguments[0]);
            }
        });
        scope.define("ord", new Function() {
            Object call(Object[] arguments) {
                return (float) (int) ((String) arguments[0]).charAt(0);
            }
        });
    }
}