package util;


import by.issoft.entity.LearningSet;

import java.nio.ByteBuffer;

public class Converter {

    public static double[] toDoubleArray(byte[] byteArray) {
        int times = Double.SIZE / Byte.SIZE;
        double[] doubles = new double[byteArray.length / times];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = ByteBuffer.wrap(byteArray, i * times, times).getDouble();
        }
        return doubles;
    }

    public static byte[] toByteArray(double[] doubleArray) {
        int times = Double.SIZE / Byte.SIZE;
        byte[] bytes = new byte[doubleArray.length * times];
        for (int i = 0; i < doubleArray.length; i++) {
            ByteBuffer.wrap(bytes, i * times, times).putDouble(doubleArray[i]);
        }
        return bytes;
    }

    public static double[] getInput(LearningSet learningSet) {
        return new double[]{
                learningSet.getUserOwner(),
                learningSet.getEventTime(),
                learningSet.getEventType(),
                learningSet.getEventWeekDay(),
                learningSet.getUserParticipant()
        };
    }

    public static double[] getOutput(LearningSet learningSet) {
        return new double[]{
                learningSet.getAccepted() ? 1 : 0
        };
    }
}
