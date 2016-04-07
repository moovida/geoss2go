package com.hydrologis.geoss2go.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Antonello
 */
public class Profile {
    public String name = "";
    public String description = "";
    public String creationdate = "";
    public String author = "";
    public String tagsPath;
    public List<String> basemapsList = new ArrayList<>();
    public List<String> spatialiteList = new ArrayList<>();

}
