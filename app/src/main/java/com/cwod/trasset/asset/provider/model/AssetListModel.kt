package com.cwod.trasset.asset.provider.model

import com.google.gson.annotations.SerializedName

/*{
{
  "data": {
    "asset_data": {
      "timestamp": "2021-04-01T12:59:48.872Z",
      "_id": "60688367158abd1068edeac5",
      "name": "White Wizard",
      "desc": "Driven by Perul, bought in 2020, used for carrying heavy weight goods",
      "type": "truck",
      "image_url": "https://previews.123rf.com/images/sylv1rob1/sylv1rob11710/sylv1rob1171001241/98422308-long-lorry-with-white-truck-and-trailer.jpg",
      "body": {
        "_id": "60688367158abd1068edeac6",
        "modelNo": "White Wizard XIV",
        "companyName": "Tata"
      },
      "lat": 26,
      "lon": 77,
      "__v": 0
    },
    "track": [
      {
        "timestamp": "2021-03-12T12:41:47.604Z",
        "_id": "60688367158abd1068edeac7",
        "lat": 25.3,
        "lon": 74.5
      },
      {
        "timestamp": "2021-04-01T12:59:48.872Z",
        "_id": "6069b6e6f6a60837e016642d",
        "lat": 26,
        "lon": 77
      }
    ],
    "geofence": {
      "geometry": {
        "type": "Polygon",
        "coordinates": [
          [
            [
              66.26953125,
              16.214674588248542
            ],
            [
              93.42773437499999,
              16.214674588248542
            ],
            [
              93.42773437499999,
              28.38173504322308
            ],
            [
              66.26953125,
              28.38173504322308
            ],
            [
              66.26953125,
              16.214674588248542
            ]
          ]
        ]
      },
      "_id": "60688367158abd1068edeac5",
      "type": "Feature",
      "__v": 0
    },
    "georoute": {
      "geometry": {
        "type": "LineString"
      }
    }
  },
  "error": {}
}
},*/
data class AssetBodyModel(
    val modelNo: String?,
    val companyName: String?,
    val address: String?,
    val employeeId: Long?
)

data class AssetModel(
    val name: String,
    val desc: String,
    val image_url: String,
    val timestamp: String,
    val type: String,
    val _id: String,
    val body: AssetBodyModel,
    var lat: Double,
    var lon: Double
)

data class TrackItemModel(
    val timestamp: String,
    val _id: String,
    var lat: Double,
    var lon: Double
)

data class GeoFenceGeometryModel(
    val type: String,
    val coordinates: List<List<List<Double>>>?
)

data class GeoFenceModel(
    val type: String,
    val geometry: GeoFenceGeometryModel
)

data class GeoRouteGeometryModel(
    val type: String,
    val coordinates: List<List<Double>>?
)

data class GeoRouteModel(
    val type: String,
    val geometry: GeoRouteGeometryModel
)


data class TrackWrapper(
    val asset_data: AssetModel,
    val track: List<TrackItemModel>,
    @SerializedName("geofence") val geoFence: GeoFenceModel?,
    @SerializedName("georoute") val geoRoute: GeoRouteModel?
)