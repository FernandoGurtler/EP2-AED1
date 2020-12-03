package main.java;

import main.java.pilha.Ingenua2;
import main.java.pilha.ListaDobrada.PilhaDobrada;
import main.java.utils.PropertyUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AppStarter {

    static PropertyUtils propertyUtils = PropertyUtils.getInstance();
    private static Map<Integer,Integer> temposIngenua = new HashMap<>();
    private static Map<Integer,Integer> temposDobrada = new HashMap<>();
    private static int runs = 100;

    public static void main(String[] args) throws IOException {

        if (args.length>1){
            runs = Integer.parseInt(args[0]);
        }

        setInputsAndOutputs();

        File folder = new File(propertyUtils.getProperty("input.entradas"));

        Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(file -> {
            String entrada = readDocument(file.getAbsolutePath());
            entrada = entrada.replace("\n",";\n");
            String[] saida = entrada.split("\n");
            System.out.println("Fazendo a "+file.getName());

            Ingenua2 ingenua2;
            StringBuilder logIngenua = null;
            long totalDobrada = 0;
            long totalIngenua = 0;
            PilhaDobrada pilhaDobrada;
            StringBuilder logDobrada = null;


            for (int i = 0; i < runs; i++) {
                ingenua2 = new Ingenua2();
                logIngenua = new StringBuilder();
                long antes = System.currentTimeMillis();
                for (String s : saida) {
                    s=s.replace(";","");
                    if (s.length()>1)
                        ingenua2.add(Integer.parseInt(s));
                    else logIngenua.append(ingenua2.remove()).append("\n");
                }
                totalIngenua += System.currentTimeMillis() - antes;

                pilhaDobrada = new PilhaDobrada();
                logDobrada = new StringBuilder();
                antes = System.currentTimeMillis();
                for (String s : saida) {
                    s=s.replace(";","");
                    if (s.length()>1)
                        pilhaDobrada.add(Integer.parseInt(s));
                    else logDobrada.append(pilhaDobrada.remove()).append("\n");
                }
                totalDobrada += System.currentTimeMillis() - antes;
            }



            try {
                writeLog(new OutPut(logIngenua, totalIngenua/runs,logDobrada,totalDobrada/runs), file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writeCSV();

        System.out.println("Finalizado sem nenhuma exeption, verifique os logs e tempos em: " +propertyUtils.getProperty("output.logs"));
    }

    private static void writeCSV() throws IOException {
        Files.createDirectories(Paths.get(propertyUtils.getProperty("output.csv")));
        FileWriter fw = new FileWriter(propertyUtils.getProperty("output.csv")+"Ingenuo-times.csv");

        fw.write("run;time\n");
        temposIngenua.forEach((run,time) -> {
            try {
                fw.write(run+";"+time+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fw.flush();
        fw.close();
        FileWriter fw2 = new FileWriter(propertyUtils.getProperty("output.csv")+"Dobrada-times.csv");

        fw2.write("run;time\n");
        temposDobrada.forEach((run, time) -> {
            try {
                fw2.write(run+";"+time+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fw2.flush();
        fw2.close();

    }

    private static void setInputsAndOutputs() throws IOException {

        String path = ClassLoader.getSystemClassLoader().getResource("main/resources/").getPath();
        propertyUtils.setProperties("input.entradas", path + propertyUtils.getProperty("input.entradas"));
        propertyUtils.setProperties("output.logs", path + propertyUtils.getProperty("output.logs"));
        propertyUtils.setProperties("output.tempos", path+propertyUtils.getProperty("output.tempos"));
        propertyUtils.setProperties("output.csv", path+propertyUtils.getProperty("output.csv"));
        Files.createDirectories(Paths.get(propertyUtils.getProperty("output.logs")));
        Files.createDirectories(Paths.get(propertyUtils.getProperty("output.tempos")));

    }


    private static String readDocument(String filename) {
        StringBuilder problem = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filename);
            while (fileReader.ready()) {
                problem.append(Character.toChars(fileReader.read()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return problem.toString();
    }

    private static int writeLog(OutPut outPut, String filename) throws IOException {

        String log = propertyUtils.getProperty("output.logs").concat(filename);
        String tempo = propertyUtils.getProperty("output.tempos").concat(filename);

        temposIngenua.put(Integer.parseInt(filename.replaceAll("[^0-9]", "")),Integer.parseInt(outPut.getTimeIngenua().replaceAll("[^0-9]", "")));
        temposDobrada.put(Integer.parseInt(filename.replaceAll("[^0-9]", "")),Integer.parseInt(outPut.getTimeDobrada().replaceAll("[^0-9]", "")));

//        Descomentar caso queira os logs limpos também

        String cleanLog = propertyUtils.getProperty("output.logs").concat("CLEAN").concat(filename);
        FileWriter fwCLEANLog = new FileWriter(cleanLog);
        fwCLEANLog.write(outPut.getOutputDobrada());
        fwCLEANLog.flush();
        fwCLEANLog.close();




        FileWriter fwLog = new FileWriter(log);
        FileWriter fwTime = new FileWriter(tempo);
        fwLog.write("Logs Ingenua2:\n"+ outPut.getOutputIngenua()+"\n\n\n----------------------------------------------\n\n\n" +
                "Logs Dobrada:\n"+ outPut.getOutputDobrada() + "\n\n\n" +
                "Arquivos iguais: " +(outPut.getOutputIngenua().compareToIgnoreCase(outPut.getOutputDobrada())==0?"Sim":"Não"));
        fwTime.write("Ingenua2: "+ outPut.getTimeIngenua() +"\n" +
                "Dobrada: "+ outPut.getTimeDobrada());
        fwLog.flush();
        fwLog.close();
        fwTime.flush();
        fwTime.close();


        return 1;
    }

}
