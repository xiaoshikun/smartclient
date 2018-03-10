package com.aspire.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaos
 * @create 2018-02-05-21:27
 */
@Component
@ConfigurationProperties(prefix = "androidconfig")
public class AndroidConfig {

private String cmd;
private String logfile;
private String appicon;
private String splashimage;
private String androidDrawableHdpi;
private String androidXhdpi;
private String androidXxhdpi;
private String androidXxxhdpi;
private String pagelayoutPath;
private String projectjsonOutPath;
private String splashImagePath;
private String defaultSelectedIconImagePath;
private String userDefinedSelectedIconImagePath;
private String smartclientjson;
private String androidjar;
private String androidSDKPath;
private String gradlePath;
private String srcOut;
private String uitemplateLibraryPath;
private String desOut;
private String osType;
private String projectresourcesPath;
private String projectZIPtargetPath;
private String deleteProjectZipPath;
private String deleteProjectPath;
private String deleteProjectApkPackagePath;
private String apkout;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getLogfile() {
        return logfile;
    }

    public void setLogfile(String logfile) {
        this.logfile = logfile;
    }

    public String getAppicon() {
        return appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;
    }

    public String getSplashimage() {
        return splashimage;
    }

    public void setSplashimage(String splashimage) {
        this.splashimage = splashimage;
    }

    public String getAndroidDrawableHdpi() {
        return androidDrawableHdpi;
    }

    public void setAndroidDrawableHdpi(String androidDrawableHdpi) {
        this.androidDrawableHdpi = androidDrawableHdpi;
    }

    public String getAndroidXhdpi() {
        return androidXhdpi;
    }

    public void setAndroidXhdpi(String androidXhdpi) {
        this.androidXhdpi = androidXhdpi;
    }

    public String getAndroidXxhdpi() {
        return androidXxhdpi;
    }

    public void setAndroidXxhdpi(String androidXxhdpi) {
        this.androidXxhdpi = androidXxhdpi;
    }

    public String getAndroidXxxhdpi() {
        return androidXxxhdpi;
    }

    public void setAndroidXxxhdpi(String androidXxxhdpi) {
        this.androidXxxhdpi = androidXxxhdpi;
    }

    public String getPagelayoutPath() {
        return pagelayoutPath;
    }

    public void setPagelayoutPath(String pagelayoutPath) {
        this.pagelayoutPath = pagelayoutPath;
    }

    public String getProjectjsonOutPath() {
        return projectjsonOutPath;
    }

    public void setProjectjsonOutPath(String projectjsonOutPath) {
        this.projectjsonOutPath = projectjsonOutPath;
    }

    public String getSplashImagePath() {
        return splashImagePath;
    }

    public void setSplashImagePath(String splashImagePath) {
        this.splashImagePath = splashImagePath;
    }

    public String getDefaultSelectedIconImagePath() {
        return defaultSelectedIconImagePath;
    }

    public void setDefaultSelectedIconImagePath(String defaultSelectedIconImagePath) {
        this.defaultSelectedIconImagePath = defaultSelectedIconImagePath;
    }

    public String getUserDefinedSelectedIconImagePath() {
        return userDefinedSelectedIconImagePath;
    }

    public void setUserDefinedSelectedIconImagePath(String userDefinedSelectedIconImagePath) {
        this.userDefinedSelectedIconImagePath = userDefinedSelectedIconImagePath;
    }

    public String getSmartclientjson() {
        return smartclientjson;
    }

    public void setSmartclientjson(String smartclientjson) {
        this.smartclientjson = smartclientjson;
    }

    public String getAndroidjar() {
        return androidjar;
    }

    public void setAndroidjar(String androidjar) {
        this.androidjar = androidjar;
    }

    public String getAndroidSDKPath() {
        return androidSDKPath;
    }

    public void setAndroidSDKPath(String androidSDKPath) {
        this.androidSDKPath = androidSDKPath;
    }

    public String getGradlePath() {
        return gradlePath;
    }

    public void setGradlePath(String gradlePath) {
        this.gradlePath = gradlePath;
    }

    public String getSrcOut() {
        return srcOut;
    }

    public void setSrcOut(String srcOut) {
        this.srcOut = srcOut;
    }

    public String getUitemplateLibraryPath() {
        return uitemplateLibraryPath;
    }

    public void setUitemplateLibraryPath(String uitemplateLibraryPath) {
        this.uitemplateLibraryPath = uitemplateLibraryPath;
    }

    public String getDesOut() {
        return desOut;
    }

    public void setDesOut(String desOut) {
        this.desOut = desOut;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getProjectresourcesPath() {
        return projectresourcesPath;
    }

    public void setProjectresourcesPath(String projectresourcesPath) {
        this.projectresourcesPath = projectresourcesPath;
    }

    public String getProjectZIPtargetPath() {
        return projectZIPtargetPath;
    }

    public void setProjectZIPtargetPath(String projectZIPtargetPath) {
        this.projectZIPtargetPath = projectZIPtargetPath;
    }

    public String getDeleteProjectZipPath() {
        return deleteProjectZipPath;
    }

    public void setDeleteProjectZipPath(String deleteProjectZipPath) {
        this.deleteProjectZipPath = deleteProjectZipPath;
    }

    public String getDeleteProjectPath() {
        return deleteProjectPath;
    }

    public void setDeleteProjectPath(String deleteProjectPath) {
        this.deleteProjectPath = deleteProjectPath;
    }

    public String getDeleteProjectApkPackagePath() {
        return deleteProjectApkPackagePath;
    }

    public void setDeleteProjectApkPackagePath(String deleteProjectApkPackagePath) {
        this.deleteProjectApkPackagePath = deleteProjectApkPackagePath;
    }

    public String getApkout() {
        return apkout;
    }

    public void setApkout(String apkout) {
        this.apkout = apkout;
    }
}
