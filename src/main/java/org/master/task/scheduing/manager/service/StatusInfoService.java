package org.master.task.scheduing.manager.service;


import org.springframework.stereotype.Service;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatusInfoService {

    // 获取主机名
    public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown Host";
        }
    }

    // 获取操作系统信息
    public String getOsName() {
        return System.getProperty("os.name");
    }

    public String getOsVersion() {
        return System.getProperty("os.version");
    }

    public String getOsArchitecture() {
        return System.getProperty("os.arch");
    }

    // 获取操作系统内存使用情况
    public Map<String, Long> getOsMemoryInfo() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemory = osBean.getTotalMemorySize() / (1024 * 1024); // MB
        long freeMemory = osBean.getFreeMemorySize() / (1024 * 1024);   // MB
        long usedMemory = totalMemory - freeMemory;

        Map<String, Long> memoryInfo = new HashMap<>();
        memoryInfo.put("usedMemoryMB", usedMemory);
        memoryInfo.put("freeMemoryMB", freeMemory);
        memoryInfo.put("totalMemoryMB", totalMemory);
        return memoryInfo;
    }

    // 获取系统 CPU 负载
    public double getSystemCpuLoad() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getCpuLoad() * 100;
    }

    // 获取 JVM 进程 CPU 负载
    public double getProcessCpuLoad() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getProcessCpuLoad() * 100;
    }
    // 获取系统时间
    public String getCurrentSystemTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // 获取系统状态信息并封装为Map
    public Map<String, Object> getServerInfo() {
        Map<String, Object> statusInfo = new HashMap<>();
        statusInfo.put("hostname", getHostname());
        statusInfo.put("osName", getOsName());
        statusInfo.put("osVersion", getOsVersion());
        statusInfo.put("osArchitecture", getOsArchitecture());
        statusInfo.put("memoryInfo", getOsMemoryInfo());
        statusInfo.put("systemCpuLoad", getSystemCpuLoad());
        statusInfo.put("processCpuLoad", getProcessCpuLoad());
        statusInfo.put("systemTime", getCurrentSystemTime());
        return statusInfo;
    }

    public Map<String, Object> getLiveStatus() {
        Map<String, Object> statusInfo = new HashMap<>();
        statusInfo.put("memoryInfo", getOsMemoryInfo());
        statusInfo.put("systemCpuLoad", getSystemCpuLoad());
        statusInfo.put("processCpuLoad", getProcessCpuLoad());
        statusInfo.put("systemTime", getCurrentSystemTime());
        return statusInfo;
    }
}
