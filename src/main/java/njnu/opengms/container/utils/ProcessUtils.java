package njnu.opengms.container.utils;

import njnu.opengms.container.bean.ProcessResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProcessUtils
 * @Description 调用其他可执行程序或脚本的工具
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class ProcessUtils {

    public static ProcessResponse StartProcess(String cwdPath, String cmd) throws IOException {
        String strcmd = cmd;
        Process ps = null;
        String rdltStr = "";
        try {
            ps = Runtime.getRuntime().exec(strcmd, null, new File(cwdPath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            InputStream is = ps.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                rdltStr += line + "\n";
            }
            ps.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = ps.exitValue();
        if (i == 0) {
            System.out.println("cmd starts run.");
        } else {
            System.out.println("cmd starts error.");
            InputStream is = ps.getErrorStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                rdltStr += line + "\n";
            }
            return new ProcessResponse(rdltStr, cmd, false);
        }
        ps.destroy();
        ps = null;

        // 批处理执行完后，根据cmd.exe进程名称
        new ProcessUtils().killProcess();
        return new ProcessResponse(rdltStr, cmd, true);
    }

    public void killProcess() {
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean callCmd(String locationCmd) throws IOException {
        String cmd = "cmd /c start  " + locationCmd;
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try {
            ps = rt.exec(cmd);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            ps.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = ps.exitValue();
        if (i == 0) {
            System.out.println("cmd starts runing.");
        } else {
            System.out.println("cmd starts error.");
            return false;
        }
        ps.destroy();
        // 批处理执行完后，根据cmd.exe进程名称
        new ProcessUtils().killProcess();
        return true;
    }

    public static String exeCmd(String commandStr){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            Process p = Runtime.getRuntime().exec("cmd /c start /b "+commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static boolean run(String exePath, String exeName,
                              String parameter) {
        if (exePath == null || exePath.equals("") || exeName == null
                || exeName.equals("")) {
            System.out.println("exePath or exeName can not be null");
            return false;
        }
        if (parameter == null || parameter.equals("")) {
            if (run(exePath, exeName)) {
                return true;
            } else {
                System.out.println("Process run error.");
                return false;
            }

        }
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", " "
                    + exeName);
            builder.directory(new File(exePath));
            Process process;
            process = builder.start();
            // 输入参数
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    process.getOutputStream()));
            bw.write(parameter);
            bw.newLine();
            bw.flush();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GB2312");
            BufferedReader br = new BufferedReader(isr);
            String line;
            String lastLine = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (lastLine.equals(line)) {
                    bw.newLine();
                    bw.flush();
                }
                lastLine = line;
            }
            process.waitFor();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static boolean run(String exePath, String exeName) {

        if (exePath == null || exePath.equals("") || exeName == null
                || exeName.equals("")) {
            System.out.println("exePath or exeName can not be null");
            return false;
        }
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", " "
                    + exeName);
            builder.directory(new File(exePath));
            Process process;
            process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static String exeCmd_Cross_platform(String commandStr){
        String os = System.getProperty("os.name");
        String result = "";
        if(os.toLowerCase().startsWith("win")){
            result = exeCmd(commandStr);
        }else if(os.toLowerCase().startsWith("linux")){
             execLinuxCdm(commandStr);
//            result = strings.get(0);
        }
        return result;
    }

    public static Object execLinuxCdm(String cmd) {
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> linuxCmd(List<String> commands)
    {
        List<String> rspList = new ArrayList<String>();
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec("/bin/bash", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands)
            {
                out.println(line);
            }

            out.println("exit");// 这个命令必须执行，否则in流不结束。
            String rspLine = "";
            while ((rspLine = in.readLine()) != null)
            {
                //System.out.println(rspLine);
                rspList.add(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return rspList;
    }


}
