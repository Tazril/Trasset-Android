package com.cwod.trasset.home.provider.model

/*{
    "data": {
    "timestamp": "2021-03-12T12:41:47.604Z",
    "name": "White Wizard",
    "desc": "Driven by Perul, bought in 2020, used for carrying heavy weight goods",
    "type": "truck",
    "image_url": "https://previews.123rf.com/images/sylv1rob1/sylv1rob11710/sylv1rob1171001241/98422308-long-lorry-with-white-truck-and-trailer.jpg",
    "body": {
    "modelNo": "White Wizard XIV",
    "companyName": "Tata"
},
    "lat": "25.3",
    "lon": "74.5"
},
    "errors": {}
},*/

data class AssetListModel(
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

