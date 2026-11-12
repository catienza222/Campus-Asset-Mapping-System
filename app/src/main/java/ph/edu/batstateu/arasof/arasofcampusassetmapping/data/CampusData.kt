package ph.edu.batstateu.arasof.arasofcampusassetmapping.data

import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.*

data class FloorData(val rooms: List<Room>, val assets: List<Asset>)

object CampusData {
    /** Size in pixels of res/drawable/campus_map.png. If you swap in a bigger picture of the same
     *  shape you can keep these numbers: tap areas scale with the picture. */
    const val MAP_W = 928f
    const val MAP_H = 1134f

    // Each building's tap area ("shapes") was traced from the map picture, in picture pixels.
    val buildings = listOf(
        Building(
            1, "Ibaan Building", Category.MIXED_USE, 641f, 1020f, 141f, 52f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(644f to 1023f, 641f to 1057f, 643f to 1070f, 771f to 1072f, 782f to 1063f, 780f to 1024f, 646f to 1020f)
            )
        ),
        Building(
            2, "VIP Corals - Nasugbu Marine Research Station", Category.RESEARCH, 822f, 1022f, 85f, 44f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(907f to 1051f, 899f to 1051f, 896f to 1047f, 896f to 1039f, 890f to 1035f, 888f to 1022f, 849f to 1023f, 848f to 1028f, 840f to 1034f, 833f to 1051f, 822f to 1053f, 822f to 1066f, 906f to 1066f)
            )
        ),
        Building(
            3, "Taal Building", Category.MIXED_USE, 815f, 830f, 72f, 145f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(875f to 830f, 817f to 837f, 815f to 840f, 820f to 861f, 822f to 904f, 831f to 906f, 832f to 911f, 839f to 916f, 839f to 946f, 849f to 950f, 847f to 959f, 832f to 963f, 833f to 973f, 836f to 975f, 879f to 970f, 879f to 954f, 887f to 951f, 884f to 848f, 878f to 841f)
            )
        ),
        Building(
            4, "Rosario Canteen", Category.AUXILIARY, 658f, 870f, 67f, 71f, floorCount = 1, roomsPerRow = 2,
            shapes = listOf(
                listOf(722f to 870f, 701f to 871f, 687f to 876f, 678f to 893f, 658f to 894f, 659f to 926f, 682f to 923f, 688f to 941f, 715f to 936f, 714f to 919f, 722f to 910f, 722f to 886f, 725f to 884f)
            )
        ),
        Building(
            5, "Apacible Museum", Category.ADMINISTRATION, 542f, 891f, 93f, 64f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(548f to 891f, 547f to 916f, 542f to 929f, 544f to 949f, 571f to 950f, 574f to 954f, 586f to 955f, 595f to 951f, 628f to 951f, 635f to 921f, 633f to 910f, 611f to 909f, 607f to 914f, 592f to 914f, 562f to 909f, 560f to 892f)
            )
        ),
        Building(
            6, "Sto. Tomas Building", Category.ACADEMIC, 444f, 729f, 99f, 333f, floorCount = 3, roomsPerRow = 3, status = BuildingStatus.UNDER_CONSTRUCTION,
            shapes = listOf(
                listOf(531f to 729f, 525f to 733f, 509f to 731f, 507f to 741f, 501f to 747f, 471f to 903f, 473f to 919f, 465f to 959f, 459f to 967f, 461f to 980f, 455f to 987f, 444f to 1050f, 448f to 1052f, 459f to 1051f, 507f to 1062f, 511f to 1058f, 526f to 1060f, 530f to 1042f, 536f to 1039f, 536f to 1031f, 531f to 1026f, 489f to 1016f, 503f to 950f, 523f to 949f, 536f to 884f, 535f to 880f, 526f to 878f, 518f to 869f, 543f to 738f, 542f to 732f)
            )
        ),
        Building(
            7, "Joson Gymnasium", Category.ACADEMIC, 567f, 722f, 95f, 181f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(583f to 722f, 579f to 728f, 567f to 859f, 568f to 868f, 579f to 869f, 576f to 874f, 575f to 897f, 634f to 903f, 636f to 874f, 649f to 874f, 662f to 728f)
            )
        ),
        Building(
            8, "Infirmary", Category.ALLIED, 716f, 738f, 49f, 114f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(725f to 738f, 719f to 741f, 716f to 851f, 746f to 852f, 749f to 850f, 750f to 823f, 747f to 810f, 751f to 804f, 753f to 768f, 765f to 764f, 765f to 740f)
            )
        ),
        Building(
            9, "Ermita Building", Category.ADMINISTRATION, 843f, 717f, 39f, 41f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(881f to 717f, 843f to 722f, 845f to 758f, 882f to 755f)
            )
        ),
        Building(
            10, "Villadolid Ground Powerhouse", Category.OTHERS, 824f, 542f, 43f, 41f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(866f to 542f, 824f to 546f, 824f to 581f, 827f to 583f, 846f to 577f, 867f to 577f)
            )
        ),
        Building(
            11, "Lobo Building", Category.ACADEMIC, 845f, 432f, 60f, 97f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(898f to 432f, 845f to 438f, 852f to 529f, 905f to 523f)
            )
        ),
        Building(
            12, "Talisay Mini Forest", Category.OTHERS, 585f, 553f, 177f, 144f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(585f to 553f, 762f to 553f, 762f to 697f, 585f to 697f)
            )
        ),
        Building(
            13, "Lipa Building", Category.ACADEMIC, 523f, 397f, 113f, 286f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(631f to 404f, 535f to 397f, 535f to 424f, 544f to 428f, 543f to 443f, 533f to 447f, 536f to 487f, 531f to 508f, 530f to 535f, 535f to 543f, 535f to 553f, 526f to 579f, 529f to 596f, 525f to 606f, 527f to 616f, 523f to 640f, 523f to 658f, 532f to 662f, 532f to 679f, 526f to 683f, 560f to 683f, 562f to 679f, 567f to 582f, 573f to 560f, 570f to 517f, 574f to 448f, 581f to 444f, 635f to 446f, 636f to 431f, 632f to 427f)
            )
        ),
        Building(
            14, "Student Council Building", Category.GENERAL, 531f, 322f, 33f, 64f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(531f to 322f, 552f to 322f, 564f to 386f, 540f to 386f)
            )
        ),
        Building(
            15, "Calatagan Building", Category.ACADEMIC, 552f, 269f, 137f, 74f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(552f to 269f, 552f to 300f, 611f to 305f, 614f to 314f, 621f to 307f, 656f to 309f, 660f to 312f, 661f to 342f, 687f to 343f, 689f to 283f, 676f to 282f, 671f to 275f, 625f to 272f, 623f to 278f, 613f to 279f, 607f to 276f, 582f to 275f, 577f to 269f)
            )
        ),
        Building(
            16, "Lemery Building", Category.ACADEMIC, 542f, 37f, 161f, 230f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(558f to 140f, 556f to 166f, 560f to 170f, 634f to 169f, 634f to 143f),
                listOf(703f to 37f, 677f to 37f, 675f to 79f, 673f to 84f, 667f to 85f, 667f to 90f, 674f to 95f, 671f to 194f, 661f to 197f, 654f to 190f, 646f to 195f, 576f to 191f, 573f to 226f, 560f to 231f, 559f to 259f, 599f to 262f, 603f to 267f, 609f to 267f, 609f to 262f, 598f to 259f, 600f to 248f, 617f to 248f, 624f to 255f, 686f to 257f, 687f to 227f, 694f to 223f),
                listOf(703f to 37f, 677f to 37f, 675f to 79f, 673f to 84f, 667f to 85f, 667f to 90f, 674f to 95f, 671f to 194f, 661f to 197f, 657f to 191f, 650f to 190f, 646f to 195f, 582f to 191f, 570f to 195f, 565f to 188f, 554f to 183f, 542f to 195f, 544f to 209f, 555f to 218f, 562f to 217f, 568f to 209f, 573f to 210f, 573f to 226f, 560f to 231f, 559f to 259f, 599f to 262f, 603f to 267f, 609f to 267f, 609f to 262f, 598f to 259f, 600f to 248f, 617f to 248f, 624f to 255f, 686f to 257f, 687f to 227f, 694f to 223f)
            )
        ),
        Building(
            17, "Maintenance Building", Category.GENERAL, 565f, 45f, 35f, 37f, floorCount = 1, roomsPerRow = 2,
            shapes = listOf(
                listOf(565f to 45f, 600f to 45f, 600f to 82f, 565f to 82f)
            )
        ),
        Building(
            18, "Open Court", Category.OTHERS, 403f, 352f, 59f, 87f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(403f to 352f, 462f to 352f, 462f to 439f, 403f to 439f)
            )
        ),
        Building(
            19, "Lian Cafeteria", Category.AUXILIARY, 320f, 576f, 113f, 60f, floorCount = 2, roomsPerRow = 2,
            shapes = listOf(
                listOf(320f to 598f, 323f to 618f, 347f to 629f, 352f to 629f, 356f to 624f, 380f to 626f, 385f to 635f, 401f to 636f, 407f to 629f, 425f to 630f, 427f to 621f, 433f to 619f, 433f to 612f, 429f to 610f, 431f to 589f, 404f to 585f, 401f to 581f, 390f to 584f, 329f to 576f, 325f to 596f)
            )
        ),
        Building(
            20, "Nasugbu Hostel", Category.MIXED_USE, 197f, 664f, 130f, 132f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(225f to 664f, 207f to 750f, 200f to 757f, 197f to 771f, 309f to 796f, 313f to 782f, 310f to 771f, 327f to 688f)
            )
        ),
        Building(
            21, "San Juan Building", Category.ACADEMIC, 312f, 686f, 102f, 339f, floorCount = 3, roomsPerRow = 3,
            shapes = listOf(
                listOf(388f to 686f, 384f to 686f, 358f to 809f, 342f to 821f, 332f to 871f, 337f to 874f, 331f to 904f, 334f to 917f, 312f to 1012f, 313f to 1025f, 318f to 1019f, 344f to 1022f, 414f to 701f, 392f to 695f)
            )
        ),
        Building(
            22, "Batangan Student Services Center", Category.MIXED_USE, 44f, 757f, 133f, 252f, floorCount = 3, roomsPerRow = 3, status = BuildingStatus.UNBUILT,
            shapes = listOf(
                listOf(108f to 757f, 104f to 759f, 44f to 971f, 54f to 974f, 62f to 982f, 63f to 990f, 71f to 992f, 73f to 997f, 113f to 1009f, 117f to 1007f, 127f to 971f, 135f to 969f, 138f to 957f, 147f to 953f, 152f to 935f, 149f to 921f, 163f to 870f, 171f to 857f, 177f to 830f, 174f to 787f, 151f to 776f, 146f to 768f)
            )
        ),
        Building(
            23, "Roxas Ground Facade", Category.GENERAL, 48f, 1037f, 85f, 29f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(48f to 1037f, 133f to 1037f, 133f to 1066f, 48f to 1066f)
            )
        ),
        Building(
            24, "RGO Commercial Rental Stall", Category.OTHERS, 130f, 998f, 110f, 70f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(130f to 998f, 240f to 998f, 240f to 1068f, 130f to 1068f)
            )
        ),
        Building(
            25, "Entrance-Exit (Guardhouse)", Category.GENERAL, 240f, 1022f, 56f, 40f, floorCount = 1, roomsPerRow = 0,
            shapes = listOf(
                listOf(240f to 1022f, 296f to 1022f, 296f to 1062f, 240f to 1062f)
            )
        )
    )

    private val cache = mutableMapOf<Pair<Int, Floor>, FloorData>()

    fun floorData(b: Building, floor: Floor): FloorData =
        cache.getOrPut(b.id to floor) { generate(b, floor) }

    /**
     * Generates a rough layout: 2 rows of rooms with a corridor between them.
     * Replace with real data (JSON / database) when you have the actual floor plans.
     */
    private fun generate(b: Building, floor: Floor): FloorData {
        val rooms = mutableListOf<Room>()
        val assets = mutableListOf<Asset>()
        val prefix = "B${b.id}-${floor.ordinal}"

        // Outdoor / open areas: no rooms, just a few safety assets
        if (b.roomsPerRow == 0) {
            assets += Asset("$prefix-HY", b.id, floor, AssetType.HYDRANT, 0.3f, 0.5f, "Fire hydrant · ${b.name}")
            assets += Asset("$prefix-FL", b.id, floor, AssetType.FIRE_LIGHT, 0.5f, 0.5f, "Fire light · ${b.name}")
            assets += Asset("$prefix-FX", b.id, floor, AssetType.EXTINGUISHER, 0.7f, 0.5f, "Fire extinguisher · ${b.name}")
            return FloorData(rooms, assets)
        }

        val perRow = b.roomsPerRow
        val digit = floor.ordinal + 1

        for (row in 0..1) for (i in 0 until perRow) {
            val k = row * perRow + i + 1
            val rw = 0.9f / perRow
            val rx = 0.05f + i * rw
            val ry = if (row == 0) 0.06f else 0.58f
            val rh = 0.36f
            val isLab = (b.id + floor.ordinal + k) % 3 == 0          // some rooms have no computers
            val computers = if (isLab) 20 + ((b.id + k) % 3) * 10 else 0
            val rid = "$prefix-R$k"
            val name = "Room $digit${k.toString().padStart(2, '0')}"

            rooms += Room(rid, b.id, floor, name, rx, ry, rw, rh, computers)
            assets += Asset("$rid-FA", b.id, floor, AssetType.FIRE_ALARM,
                rx + rw * 0.25f, ry + rh * 0.68f, "Fire alarm · $name", rid)
            assets += Asset("$rid-FL", b.id, floor, AssetType.FIRE_LIGHT,
                rx + rw * 0.75f, ry + rh * 0.68f, "Fire light · $name", rid)
            if (computers > 0) {
                assets += Asset("$rid-PC", b.id, floor, AssetType.COMPUTER,
                    rx + rw * 0.5f, ry + rh * 0.38f, "Computers ($computers) · $name", rid)
            }
        }

        // Corridor assets
        assets += Asset("$prefix-EX1", b.id, floor, AssetType.EXIT, 0.03f, 0.5f, "Exit (west) · ${b.name}")
        assets += Asset("$prefix-EX2", b.id, floor, AssetType.EXIT, 0.97f, 0.5f, "Exit (east) · ${b.name}")
        assets += Asset("$prefix-CFL", b.id, floor, AssetType.FIRE_LIGHT, 0.25f, 0.5f, "Corridor fire light · ${b.name}")
        assets += Asset("$prefix-CFA", b.id, floor, AssetType.FIRE_ALARM, 0.75f, 0.5f, "Corridor fire alarm · ${b.name}")
        if (floor == Floor.GROUND) {
            assets += Asset("$prefix-HY", b.id, floor, AssetType.HYDRANT, 0.5f, 0.5f, "Fire hydrant · ${b.name}")
        } else {
            assets += Asset("$prefix-FX", b.id, floor, AssetType.EXTINGUISHER, 0.5f, 0.5f, "Fire extinguisher · ${b.name} ${floor.label}")
        }
        return FloorData(rooms, assets)
    }
}