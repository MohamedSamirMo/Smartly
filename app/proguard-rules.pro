# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# الحفاظ على النشاطات (Activities)
-keep class * extends android.app.Activity { *; }

# الحفاظ على الأجزاء (Fragments)
-keep class * extends android.app.Fragment { *; }

# منع تقليص واجهات البرمجة (APIs) العامة
-keep public class com.smartly.newapp.** { public *; }

# الحفاظ على مكتبة Gson
-keep class com.google.gson.** { *; }

# الحفاظ على مكتبة Retrofit
-keep class retrofit2.** { *; }

# السماح لجميع الإضافات الخاصة بالكلاس بالبقاء
-keepattributes InnerClasses

# تجاهل التحذيرات
-dontwarn android.support.v4.**

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile