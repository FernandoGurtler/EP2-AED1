package main.java;

public class OutPut {
    private String outputIngenua;
    private String timeIngenua;
    private String outputDobrada;
    private String timeDobrada;


    public String getOutputDobrada() {
        return outputDobrada;
    }

    public void setOutputDobrada(String outputDobrada) {
        this.outputDobrada = outputDobrada;
    }

    public String getTimeDobrada() {
        return timeDobrada;
    }

    public void setTimeDobrada(String timeDobrada) {
        this.timeDobrada = timeDobrada;
    }

    public OutPut(StringBuilder outputIngenua, long timeIngenua, StringBuilder outputDobrada, long timeDobrada) {
        this.outputIngenua = outputIngenua.toString();
        this.timeIngenua = timeIngenua + " ms";
        this.outputDobrada = outputDobrada.toString();
        this.timeDobrada = timeDobrada + " ms";
    }

    public String getOutputIngenua() {
        return outputIngenua;
    }

    public void setOutputIngenua(String outputIngenua) {
        this.outputIngenua = outputIngenua;
    }

    public String getTimeIngenua() {
        return timeIngenua;
    }

    public void setTimeIngenua(String timeIngenua) {
        this.timeIngenua = timeIngenua;
    }
}
