package com.happylittleapps.osrs.quickerpickerupper.task;

import java.util.Comparator;

import org.osbot.rs07.api.model.GroundItem;

import com.happylittleapps.osrs.quickerpickerupper.QuickerPickerUpper;

public class DesirableItem {
	
	private QuickerPickerUpper _curScript;
	private GroundItem _groundItem;
	private long _value;
	
	long minValue = 1000;
	
	int[] highValueItemIds = { 6,8,10,12,70,97,99,101,103,105,107,109,111,139,141,143,153,157,159,169,171,189,191,
			193,207,211,213,215,217,219,225,245,257,261,263,265,267,269,389,391,397,449,451,534,536,540,569,571,573,
			575,577,581,751,811,817,824,859,962,981,985,987,989,991,993,995,1031,1033,1038,1040,1042,1044,1046,1048,1050,
			1053,1055,1057,1065,1071,1073,1077,1079,1085,1089,1091,1093,1099,1107,1111,1113,1121,1123,1125,1127,1135,
			1147,1149,1161,1163,1183,1185,1187,1195,1197,1199,1201,1213,1215,1229,1231,1233,1245,1247,1249,1251,1253,
			1255,1259,1261,1263,1269,1271,1275,1287,1289,1301,1303,1305,1317,1319,1327,1331,1333,1345,1347,1359,1371,
			1373,1377,1381,1383,1385,1387,1391,1393,1395,1397,1399,1401,1403,1405,1407,1432,1434,1442,1448,1454,1456,
			1462,1470,1472,1474,1476,1478,1513,1601,1603,1613,1615,1617,1619,1629,1631,1641,1643,1645,1660,1662,1664,
			1679,1681,1683,1698,1700,1702,1704,1712,1725,1731,1745,1747,1749,1751,1753,1959,1961,1989,2064,2084,2187,
			2191,2203,2209,2217,2341,2343,2361,2363,2366,2368,2434,2440,2442,2444,2450,2474,2476,2481,2483,2485,2487,
			2489,2491,2493,2495,2497,2499,2501,2503,2505,2507,2509,2550,2568,2570,2572,2577,2579,2581,2583,2585,2587,
			2589,2591,2593,2595,2597,2599,2601,2603,2605,2607,2609,2611,2613,2615,2617,2619,2621,2623,2625,2627,2629,
			2631,2633,2635,2637,2639,2641,2643,2645,2647,2651,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,
			2675,2876,2890,2894,2898,2900,2902,2904,2906,2908,2910,2914,2920,2934,2940,2997,2998,3000,3002,3004,3016,
			3018,3020,3024,3026,3028,3030,3032,3049,3051,3053,3054,3100,3101,3107,3122,3123,3125,3140,3159,3194,3196,
			3198,3200,3202,3204,3331,3335,3337,3341,3343,3349,3357,3361,3385,3387,3389,3391,3393,3402,3404,3430,3432,
			3438,3440,3442,3444,3446,3448,3470,3472,3473,3474,3475,3476,3477,3478,3479,3480,3481,3483,3485,3486,3488,
			3678,3749,3751,3753,3755,3759,3763,3765,3769,3771,3773,3775,3777,3779,3781,3789,3797,3801,3827,3828,3829,
			3830,3831,3832,3833,3834,3835,3836,3837,3838,3853,4087,4089,4091,4093,4095,4097,4099,4101,4103,4105,4107,
			4109,4111,4113,4115,4117,4121,4129,4131,4151,4153,4156,4170,4207,4212,4224,4304,4306,4308,4310,4417,4419,
			4421,4436,4532,4548,4582,4585,4587,4627,4675,4684,4708,4710,4712,4714,4716,4718,4720,4722,4724,4726,4728,
			4730,4732,4734,4736,4738,4745,4747,4749,4751,4753,4755,4757,4759,4821,4827,4830,4832,4834,4846,4850,4860,
			4866,4872,4878,4884,4890,4896,4902,4908,4914,4920,4926,4932,4938,4944,4950,4956,4962,4968,4974,4980,4986,
			4992,4998,5014,5024,5026,5028,5030,5032,5034,5036,5038,5040,5042,5044,5046,5048,5050,5052,5075,5287,5288,
			5289,5295,5296,5298,5299,5300,5302,5303,5304,5313,5314,5315,5316,5345,5371,5372,5373,5374,5386,5500,5501,
			5502,5516,5547,5574,5575,5576,5624,5627,5634,5641,5666,5668,5670,5672,5674,5676,5678,5680,5682,5686,5688,
			5690,5692,5694,5696,5698,5700,5710,5712,5714,5716,5720,5724,5726,5728,5730,5734,5736,5741,5743,5747,5749,
			5751,5753,5755,5757,5761,5765,5777,5785,5793,5801,5809,5817,5825,5833,5841,5849,5857,5865,5873,5881,5889,
			5897,5905,5913,5921,5929,5933,5937,5940,5943,5945,5947,5949,6016,6051,6128,6129,6130,6131,6133,6135,6137,
			6139,6141,6143,6147,6149,6153,6157,6159,6161,6163,6165,6167,6169,6171,6173,6211,6213,6215,6235,6237,6257,
			6259,6279,6311,6315,6317,6335,6337,6339,6341,6343,6345,6347,6349,6351,6353,6355,6357,6359,6361,6363,6365,
			6367,6369,6371,6373,6375,6377,6379,6390,6400,6404,6406,6412,6416,6418,6420,6522,6523,6524,6525,6526,6527,
			6528,6562,6563,6568,6571,6573,6575,6577,6579,6581,6583,6585,6587,6589,6599,6603,6605,6607,6609,6611,6613,
			6615,6617,6619,6621,6623,6625,6627,6629,6631,6633,6685,6687,6689,6693,6724,6729,6731,6733,6735,6737,6739,
			6750,6752,6760,6762,6764,6809,6812,6889,6908,6910,6912,6914,6916,6918,6920,6922,6924,6959,6977,7060,7110,
			7116,7128,7138,7158,7186,7196,7206,7216,7218,7228,7319,7321,7323,7325,7327,7332,7334,7336,7338,7340,7342,
			7346,7348,7350,7352,7354,7356,7358,7360,7362,7364,7366,7368,7370,7372,7374,7376,7378,7380,7382,7384,7386,
			7388,7390,7392,7394,7396,7398,7399,7400,7416,7418,7439,7441,7443,7445,7447,7449,7451,7468,7568,7668,7759,
			7761,7763,7765,7767,7769,7771,7939,8015,8417,8419,8421,8423,8425,8427,8429,8431,8433,8435,8437,8439,8441,
			8443,8445,8447,8449,8451,8453,8455,8457,8459,8461,8508,8518,8520,8522,8526,8556,8560,8568,8570,8574,8586,
			8588,8592,8594,8606,8608,8620,8622,8628,8780,8782,8784,8786,8788,8792,8872,8874,8876,8878,8880,8901,8921,
			8924,8925,8927,8928,9034,9044,9050,9183,9185,9194,9245,9298,9303,9304,9305,9342,9419,9429,9431,9463,9465,
			9469,9470,9472,9629,9634,9636,9638,9640,9642,9644,9666,9668,9670,9672,9674,9676,9678,9729,9731,9733,9846,
			9847,9848,9851,9857,9858,9861,9867,9998,10000,10002,10004,10014,10033,10034,10035,10037,10039,10041,10043,
			10045,10047,10049,10051,10053,10055,10057,10059,10061,10063,10065,10067,10071,10075,10079,10081,10083,10085,
			10095,10099,10103,10107,10109,10111,10121,10123,10127,10132,10134,10156,10280,10284,10286,10288,10290,10292,
			10294,10296,10298,10300,10302,10304,10306,10308,10310,10312,10314,10316,10318,10320,10322,10324,10330,10332,
			10334,10336,10338,10340,10342,10344,10346,10348,10350,10352,10354,10362,10364,10366,10368,10370,10372,10374,
			10376,10378,10380,10382,10384,10386,10388,10390,10392,10394,10396,10398,10400,10402,10404,10406,10408,10410,
			10412,10414,10416,10418,10420,10422,10424,10426,10428,10430,10432,10434,10436,10440,10442,10444,10446,10448,
			10450,10452,10454,10456,10458,10460,10462,10464,10466,10468,10470,10472,10474,10476,10496,10564,10589,10808,
			10826,10828,10925,10927,10929,10931,10937,10954,10958,11037,11061,11085,11088,11090,11092,11095,11105,11113,
			11115,11118,11126,11128,11130,11133,11200,11212,11227,11228,11229,11230,11231,11232,11233,11234,11235,11237,
			11238,11240,11242,11244,11246,11248,11250,11252,11254,11256,11260,11280,11284,11286,11335,11375,11377,11379,
			11382,11384,11386,11389,11391,11393,11396,11398,11400,11403,11405,11407,11410,11412,11414,11417,11419,11433,
			11435,11441,11443,11449,11451,11461,11465,11467,11471,11473,11475,11481,11485,11487,11493,11495,11501,11503,
			11509,11513,11515,11785,11787,11789,11791,11798,11802,11804,11806,11808,11810,11812,11814,11816,11818,11820,
			11822,11824,11826,11828,11830,11832,11834,11836,11838,11840,11889,11902,11905,11908,11920,11924,11926,11928,
			11929,11930,11931,11932,11933,11934,11936,11943,11959,11960,11964,11968,11972,11978,11980,11990,11992,11998,
			12000,12002,12004,12007,12193,12195,12197,12199,12201,12203,12205,12207,12209,12211,12213,12215,12217,12219,
			12221,12223,12225,12227,12229,12231,12233,12235,12237,12239,12241,12243,12245,12247,12249,12251,12253,12255,
			12257,12259,12261,12263,12265,12267,12269,12271,12273,12275,12277,12279,12281,12283,12285,12287,12289,12291,
			12293,12295,12297,12299,12301,12303,12305,12307,12309,12311,12313,12315,12317,12319,12321,12323,12325,12327,
			12329,12331,12333,12335,12337,12339,12341,12343,12345,12347,12349,12351,12353,12355,12357,12359,12361,12363,
			12365,12367,12369,12371,12373,12375,12379,12381,12383,12385,12387,12389,12391,12393,12395,12397,12399,12402,
			12403,12404,12405,12406,12408,12409,12410,12412,12422,12424,12426,12428,12430,12432,12434,12437,12439,12441,
			12443,12445,12447,12449,12451,12453,12455,12460,12462,12464,12466,12468,12470,12472,12474,12476,12478,12480,
			12482,12484,12486,12488,12490,12492,12494,12496,12498,12500,12502,12504,12506,12508,12510,12512,12514,12516,
			12518,12520,12522,12524,12526,12528,12530,12532,12534,12536,12538,12540,12596,12598,12601,12603,12605,12613,
			12614,12615,12616,12617,12618,12619,12620,12621,12622,12623,12624,12625,12627,12629,12631,12633,12635,12640,
			12642,12695,12697,12699,12701,12757,12759,12761,12763,12769,12771,12775,12776,12777,12778,12779,12780,12781,
			12782,12783,12786,12789,12798,12800,12802,12804,12817,12819,12821,12823,12825,12827,12829,12831,12833,12846,
			12849,12851,12863,12865,12867,12869,12871,12873,12875,12877,12879,12881,12883,12885,12900,12902,12905,12907,
			12909,12911,12913,12915,12917,12919,12922,12924,12927,12929,12932,12936,12938,12964,12966,12968,12970,12976,
			12978,12980,12982,12984,12986,12988,12990,12992,12994,12996,12998,13000,13002,13004,13006,13008,13010,13012,
			13014,13016,13018,13020,13022,13024,13026,13028,13030,13032,13034,13036,13038,13040,13042,13044,13046,13048,
			13050,13052,13054,13056,13058,13060,13062,13064,13066,13149,13151,13153,13155,13157,13159,13161,13163,13165,
			13167,13169,13171,13173,13175,13190,13227,13229,13231,13233,13235,13237,13239,13245,13256,13263,13265,13267,
			13269,13271,13277,13383,13385,13387,13389,13439,13441,13466,13469,13472,13475,13478,13481,13484,13487,13490,
			13493,13496,13499,13502,13505,13508,13511,13576,13652,13657,19478,19481,19484,19486,19488,19490,19493,19496,
			19501,19529,19532,19535,19538,19541,19544,19547,19550,19553,19582,19586,19589,19592,19595,19598,19601,19604,
			19607,19610,19613,19615,19617,19619,19621,19623,19625,19627,19629,19631,19656,19662,19672,19701,19707,19724,
			19727,19912,19915,19918,19921,19924,19927,19930,19933,19936,19943,19946,19949,19952,19955,19958,19961,19964,
			19967,19970,19973,19976,19979,19982,19985,19988,19991,19994,19997,20002,20005,20008,20011,20014,20017,20020,
			20023,20026,20029,20032,20035,20038,20041,20044,20047,20050,20053,20056,20059,20062,20065,20068,20071,20074,
			20077,20080,20083,20086,20089,20092,20095,20098,20101,20104,20107,20110,20113,20116,20119,20122,20125,20128,
			20131,20134,20137,20140,20143,20146,20149,20152,20155,20158,20161,20166,20169,20172,20175,20178,20181,20184,
			20187,20190,20193,20196,20199,20202,20205,20208,20211,20214,20217,20220,20223,20226,20229,20232,20235,20240,
			20243,20246,20251,20254,20260,20263,20266,20269,20272,20275,20376,20379,20382,20385,20433,20436,20439,20442,
			20517,20520,20590,20595,20716,20718,20724,20727,20730,20733,20736,20739,20749,20756,20849,21000,21003,21006,
			21009,21012,21015,21018,21021,21024,21028,21034,21043,21047,21049,21079,21081,21084,21087,21090,21093,21096,
			21105,21114,21123,21146,21163,21166,21183 }; // VALUE > 1000

	public DesirableItem(QuickerPickerUpper curScript, GroundItem item) {
		this._curScript = curScript; 
		minValue = this._curScript.getMinimumValue();
		this._groundItem = item;
	}

	public boolean isDesirable() {
		for (TradeableItem i : this._curScript.allTradeableItems) {
			if (this._groundItem.getId() == i.getItemId()) {
				if (i.getItemId() == 995) {
					this._value = this._groundItem.getAmount();					
				} else {
					this._value = i.getValue();
				}				
				this._curScript.log(this._groundItem.getName() + " has a value of " + this._value);
				if (this._value >= minValue) {
					return true;
				}
			}			
		}
		
		return false;
	}
	
	public void setGroundItem(GroundItem item) {
		this._groundItem = item;
	}
	
	public GroundItem getGroundItem() {
		return this._groundItem;
	}
	
	public long getValue() {
		return this._value;
	}

	public double getDistance () {        
	    double ac = Math.abs(this._groundItem.getPosition().getY() - this._curScript.myPlayer().getPosition().getY());
	    double cb = Math.abs(this._groundItem.getPosition().getX() - this._curScript.myPlayer().getPosition().getX());
	         
	    return Math.hypot(ac, cb);
	}
}

class SortByValue implements Comparator<DesirableItem> {

	@Override
	public int compare(DesirableItem arg0, DesirableItem arg1) {
		if (arg0.getValue() < arg1.getValue()) {
			return -1;
		}
		
		if (arg0.getValue() > arg1.getValue()) {
			return 1;
		}
		
		return 0;
	}	
}

class SortByDistance implements Comparator<DesirableItem> {

	@Override
	public int compare(DesirableItem arg0, DesirableItem arg1) {
		if (arg0.getDistance() < arg1.getDistance()) {
			return -1;
		}
		
		if (arg0.getDistance() > arg1.getDistance()) {
			return 1;
		}
		
		return 0;
	}	
}