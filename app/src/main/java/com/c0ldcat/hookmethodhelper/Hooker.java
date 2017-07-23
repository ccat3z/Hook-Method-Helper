package com.c0ldcat.hookmethodhelper;

import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.ArrayList;

public class Hooker implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // read shared preference
        XSharedPreferences xSharedPreferences = new XSharedPreferences(Common.PACKAGE_NAME);
        xSharedPreferences.makeWorldReadable();

        // match package
        String targetPackage = xSharedPreferences.getString("package", "null");
        if (!loadPackageParam.packageName.equals(targetPackage)) return;

        // get target class and method
        final String targetClass = xSharedPreferences.getString("class", "null");
        final String targetMethod = xSharedPreferences.getString("method", "null");

        // process method
        String targetMethodStrings[] = targetMethod.replace(")", "").split("\\(");

        String targetMethodName = targetMethodStrings[0];

        ArrayList<Object> targerMethodParams = new ArrayList<>();

        if (targetMethodStrings.length == 2) {
            for (String param : targetMethodStrings[1].split(",")) {
                try {
                    targerMethodParams.add(XposedHelpers.findClass(param.replace(" ", ""), loadPackageParam.classLoader));
                } catch (XposedHelpers.ClassNotFoundError e) {
                    Log.d(Common.DEBUG_TAG, param + " in " + targetMethod + " no found");
                    return;
                }
            }
        }

        // add hooker
        targerMethodParams.add(new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(Common.DEBUG_TAG, targetMethod + " of " + targetClass + " is called");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });

        // try to hook method
        try {
            XposedHelpers.findAndHookMethod(targetClass, loadPackageParam.classLoader, targetMethodName, targerMethodParams.toArray());

            Log.d(Common.DEBUG_TAG, "find and hook " + targetMethod + " of " + targetClass + " successfully");
        } catch (XposedHelpers.ClassNotFoundError e) {
            Log.d(Common.DEBUG_TAG, targetClass + " not found");
        } catch (NoSuchMethodError e) {
            Log.d(Common.DEBUG_TAG, targetMethod + " of " + targetClass + " not found");
        }
    }
}
