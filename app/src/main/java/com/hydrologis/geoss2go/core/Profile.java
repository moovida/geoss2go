package com.hydrologis.geoss2go.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Antonello
 */
public class Profile {
    public String name = "new profile";
    public String description = "new profile description";
    public String creationdate = "";
    public boolean active = false;
    public String color = "#FFFFFF";
    public String tagsPath;
    public List<String> basemapsList = new ArrayList<>();
    public List<String> spatialiteList = new ArrayList<>();

}
