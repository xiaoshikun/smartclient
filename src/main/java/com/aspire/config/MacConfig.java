package com.aspire.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaos
 * @create 2018-02-06-10:30
 */
@Component
@ConfigurationProperties(prefix = "macconfig")
public class MacConfig {
    private String iosCmd;
    private String iosRes;
    private String iospagelayoutPath;
    private String iosprojectjsonOutPath;
    private String iosprojectresourcesPath;
    private String iosprojectZIPtargetPath;
    private String ioslogfile;
    private String iosClearOldProject;
    private String iosdefaultSelectedIconImagePath;
    private String deleteiosContents;
    private String deleteiosshell;
    private String deleteiosMyApplicationxcodeproj;
    private String deleteiosaa;
    private String deleteiosMyApplication;
    private String deleteiosprojectzip;

    public String getIosCmd() {
        return iosCmd;
    }

    public void setIosCmd(String iosCmd) {
        this.iosCmd = iosCmd;
    }

    public String getIosRes() {
        return iosRes;
    }

    public void setIosRes(String iosRes) {
        this.iosRes = iosRes;
    }

    public String getIospagelayoutPath() {
        return iospagelayoutPath;
    }

    public void setIospagelayoutPath(String iospagelayoutPath) {
        this.iospagelayoutPath = iospagelayoutPath;
    }

    public String getIosprojectjsonOutPath() {
        return iosprojectjsonOutPath;
    }

    public void setIosprojectjsonOutPath(String iosprojectjsonOutPath) {
        this.iosprojectjsonOutPath = iosprojectjsonOutPath;
    }

    public String getIosprojectresourcesPath() {
        return iosprojectresourcesPath;
    }

    public void setIosprojectresourcesPath(String iosprojectresourcesPath) {
        this.iosprojectresourcesPath = iosprojectresourcesPath;
    }

    public String getIosprojectZIPtargetPath() {
        return iosprojectZIPtargetPath;
    }

    public void setIosprojectZIPtargetPath(String iosprojectZIPtargetPath) {
        this.iosprojectZIPtargetPath = iosprojectZIPtargetPath;
    }

    public String getIoslogfile() {
        return ioslogfile;
    }

    public void setIoslogfile(String ioslogfile) {
        this.ioslogfile = ioslogfile;
    }

    public String getIosClearOldProject() {
        return iosClearOldProject;
    }

    public void setIosClearOldProject(String iosClearOldProject) {
        this.iosClearOldProject = iosClearOldProject;
    }

    public String getIosdefaultSelectedIconImagePath() {
        return iosdefaultSelectedIconImagePath;
    }

    public void setIosdefaultSelectedIconImagePath(String iosdefaultSelectedIconImagePath) {
        this.iosdefaultSelectedIconImagePath = iosdefaultSelectedIconImagePath;
    }

    public String getDeleteiosContents() {
        return deleteiosContents;
    }

    public void setDeleteiosContents(String deleteiosContents) {
        this.deleteiosContents = deleteiosContents;
    }

    public String getDeleteiosshell() {
        return deleteiosshell;
    }

    public void setDeleteiosshell(String deleteiosshell) {
        this.deleteiosshell = deleteiosshell;
    }

    public String getDeleteiosMyApplicationxcodeproj() {
        return deleteiosMyApplicationxcodeproj;
    }

    public void setDeleteiosMyApplicationxcodeproj(String deleteiosMyApplicationxcodeproj) {
        this.deleteiosMyApplicationxcodeproj = deleteiosMyApplicationxcodeproj;
    }

    public String getDeleteiosaa() {
        return deleteiosaa;
    }

    public void setDeleteiosaa(String deleteiosaa) {
        this.deleteiosaa = deleteiosaa;
    }

    public String getDeleteiosMyApplication() {
        return deleteiosMyApplication;
    }

    public void setDeleteiosMyApplication(String deleteiosMyApplication) {
        this.deleteiosMyApplication = deleteiosMyApplication;
    }

    public String getDeleteiosprojectzip() {
        return deleteiosprojectzip;
    }

    public void setDeleteiosprojectzip(String deleteiosprojectzip) {
        this.deleteiosprojectzip = deleteiosprojectzip;
    }
}
