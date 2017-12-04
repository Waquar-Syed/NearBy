package `in`.nj.nearby.model

/**
 * Created by rishikapriya on 04/12/17.
 */


class POSDetails(
        var MID : String,
        var TID : String,
        var MCC_CDE : String,
        var MCC_DSC : String,
        var OUTER_POSTAL_CODE : String,
        var INNER_POSTAL_CODE : String,
        var PREMISE_NUM : String,
        var PREMISE_NME : String,
        var STREET_NUM : String,
        var STREET_NME : String,
        var DISTRICT_NME : String,
        var CITY_NME : String,
        var M_NAME : String
);

class POS(
        var message : String,
        var status : Int,
        var pos : List<POSDetails>
);

class Coordinates(
        var results: List<Result>,
        var status: String
);

class Result(
        var formatted_address : String,
        var geometry : Geometry
);

class Geometry(
        var location : Location
);

class Location(
        var lat : Double,
        var lng : Double
);