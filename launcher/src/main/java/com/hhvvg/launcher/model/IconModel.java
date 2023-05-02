package com.hhvvg.launcher.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Xml;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.hhvvg.launcher.utils.Logger;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconModel {
    private static final String DRAWABLE_XML = "drawable.xml";
    private static final String APP_FILTER = "appfilter";
    private static final Pattern sComponentPattern = Pattern.compile("ComponentInfo\\{(.*)\\}");

    public void loadAllIcons(String iconPkg, Context context, Map<ComponentName, LauncherIconResource> outIcons) {
        if (iconPkg == null) {
            return;
        }
        try {
            Context pkgContext = context.createPackageContext(iconPkg, Context.CONTEXT_IGNORE_SECURITY);
            Resources iconResources = context.getPackageManager().getResourcesForApplication(iconPkg);
            int filterId = iconResources.getIdentifier(
                    APP_FILTER,
                    "xml",
                    iconPkg
            );
            XmlPullParser parser;
            if (filterId != 0) {
                parser = iconResources.getXml(filterId);
            } else {
                InputStream is = pkgContext.getAssets().open(DRAWABLE_XML);
                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(is, null);
            }
            loadXmlPullParser(iconPkg, iconResources, parser, outIcons);
        } catch (Exception e) {
            Logger.log("Error loading icons", e);
        }
    }

    @Nullable
    public Drawable getDrawable(LauncherIconResource iconResource) {
        int id = iconResource.resources.getIdentifier(
                iconResource.drawable,
                "drawable",
                iconResource.iconPkg
        );
        if (id == 0) {
            return null;
        }
        try {
            return ResourcesCompat.getDrawable(iconResource.resources, id, null);
        } catch (Exception e) {
            Logger.log("Error getting drawable", e);
            return null;
        }
    }

    private void loadXmlPullParser(String iconPkg, Resources res, XmlPullParser parser, Map<ComponentName, LauncherIconResource> outIcons) throws Exception {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                String component = parser.getAttributeValue(null, "component");
                if (component == null) {
                    parser.next();
                    continue;
                }
                Matcher m = sComponentPattern.matcher(component);
                if (m.find()) {
                    component = m.group(1);
                } else {
                    parser.next();
                    continue;
                }
                String drawableName = parser.getAttributeValue(null, "drawable");
                if (drawableName == null) {
                    parser.next();
                    continue;
                }
                ComponentName cn = ComponentName.unflattenFromString(component);
                if (cn == null) {
                    parser.next();
                    continue;
                }
                LauncherIconResource icon = new LauncherIconResource(
                        iconPkg,
                        res,
                        cn,
                        drawableName
                );
                outIcons.put(cn, icon);
            }
            parser.next();
        }
    }

    public static class LauncherIconResource {
        public final String iconPkg;
        public final Resources resources;
        public final ComponentName componentName;
        public final String drawable;

        public LauncherIconResource(String iconPkg, Resources res, ComponentName cn, String drawable) {
            this.iconPkg = iconPkg;
            this.resources = res;
            this.componentName = cn;
            this.drawable = drawable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LauncherIconResource that = (LauncherIconResource) o;

            if (!iconPkg.equals(that.iconPkg)) return false;
            if (!resources.equals(that.resources)) return false;
            return componentName.equals(that.componentName);
        }

        @Override
        public int hashCode() {
            int result = iconPkg.hashCode();
            result = 31 * result + resources.hashCode();
            result = 31 * result + componentName.hashCode();
            return result;
        }
    }
}
