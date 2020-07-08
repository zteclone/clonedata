# -- ----------------------------
# -- clone_dict
# -- 字典
# -- ----------------------------
INSERT IGNORE INTO `clone_dict` VALUES ('1', 'maoyan_countrys_json', '{\"11\":\"德国\",\"00\":\"其他\",\"13\":\"中国台湾\",\"14\":\"俄罗斯\",\"16\":\"意大利\",\"17\":\"西班牙\",\"19\":\"波兰\",\"02\":\"大陆\",\"03\":\"美国\",\"04\":\"法国\",\"05\":\"英国\",\"06\":\" 日本 \",\"07\":\"韩国\",\"08\":\"印度\",\"09\":\"泰国\",\"20\":\"澳大利亚\",\"10\":\"中国香港\",\"21\":\"伊朗\"}', '猫眼国家转换');
INSERT IGNORE INTO `clone_dict` VALUES ('2', 'maoyan_years_json', '{\"11\":\"2016\",\"12\":\"2017\",\"13\":\"2018\",\"14\":\"2019\",\"15\":\"2020\",\"01\":\"更早\",\"02\":\" 70年代 \",\"03\":\"80年代\",\"04\":\"90年代\",\"05\":\"2000-2010\",\"06\":\"2011\",\"07\":\"2012\",\"08\":\"2013\",\"09\":\"2014\",\"10\":\"2015\"}', '猫眼年份转换');



# -- ----------------------------
# -- task_management
# -- 豆瓣电影
# -- ----------------------------
# INSERT IGNORE INTO `task_management` VALUES ('33b0e4e99eec4fafa266b69770cf76dc', 'http://localhost:8090/douban/doubanMv?paramType=010201', '', '0 0 0 ? * 2', '33b0e4e99eec4fafa266b69770cf76dc', '豆瓣  电影  中国大陆  2019-2020', '1', 'http-get', '30000', '1');
# INSERT IGNORE INTO `task_management` VALUES ('441672e3088f4ecb8f6ea6118197d21b', 'http://localhost:8090/douban/doubanMv?paramType=011312', '', '0 0 2 ? * 2', '441672e3088f4ecb8f6ea6118197d21b', '豆瓣  电影  中国大陆  2000-2009', '1', 'http-get', '30000', '5');
# INSERT IGNORE INTO `task_management` VALUES ('54b03afbb34948bb8bea024827134abc', 'http://localhost:8090/douban/doubanMv?paramType=010806', '', '0 0 1 ? * 6', '54b03afbb34948bb8bea024827134abc', '豆瓣  电影  中国大陆  2013-2015', '1', 'http-get', '30000', '3');
# INSERT IGNORE INTO `task_management` VALUES ('67222da9e0ef46f19d38f47c1688bd7c', 'http://localhost:8090/douban/doubanMv?paramType=010503', '', '0 30 0 ? * 6', '67222da9e0ef46f19d38f47c1688bd7c', '豆瓣  电影  中国大陆  2016-2018', '1', 'http-get', '30000', '2');
# INSERT IGNORE INTO `task_management` VALUES ('aerlan2', 'http://localhost:8090/douban/doubanMv?paramType=182201', '', '0 30 18 ? * 6', 'aerlan2', '豆瓣  电影  爱尔兰  1-2020', '1', 'http-get', '30000', '38');
# INSERT IGNORE INTO `task_management` VALUES ('aodaliyamv2', 'http://localhost:8090/douban/doubanMv?paramType=172201', '', '0 0 19 ? * 6', 'aodaliyamv2', '豆瓣  电影  澳大利亚  1-2020', '1', 'http-get', '30000', '39');
# INSERT IGNORE INTO `task_management` VALUES ('baximv2', 'http://localhost:8090/douban/doubanMv?paramType=202201', '', '0 0 22 ? * 6', 'baximv2', '豆瓣  电影  巴西  1-2020', '1', 'http-get', '30000', '46');
# INSERT IGNORE INTO `task_management` VALUES ('c67200fc74a74bfa99b042370177690e', 'http://localhost:8090/douban/doubanMv?paramType=011109', '', '0 30 1 ? * 6', 'c67200fc74a74bfa99b042370177690e', '豆瓣  电影  中国大陆  2010-2012', '1', 'http-get', '30000', '4');
# INSERT IGNORE INTO `task_management` VALUES ('d5c831a2ea42401a8056d47546f99ba8', 'http://localhost:8090/douban/doubanMv?paramType=011615', '', '0 30 2 ? * 6', 'd5c831a2ea42401a8056d47546f99ba8', '豆瓣  电影  中国大陆  1980-1999', '1', 'http-get', '30000', '6');
# INSERT IGNORE INTO `task_management` VALUES ('danmaimv2', 'http://localhost:8090/douban/doubanMv?paramType=212201', '', '0 40 23 ? * 6', 'danmaimv2', '豆瓣  电影  丹麦  1-2020', '1', 'http-get', '30000', '51');
# INSERT IGNORE INTO `task_management` VALUES ('dgmv1', 'http://localhost:8090/douban/doubanMv?paramType=091101', '', '0 20 21 ? * 6', 'dgmv1', '豆瓣  电影  德国  2010-2020', '1', 'http-get', '30000', '44');
# INSERT IGNORE INTO `task_management` VALUES ('dgmv2', 'http://localhost:8090/douban/doubanMv?paramType=092212', '', '0 40 21 ? * 6', 'dgmv2', '豆瓣  电影  德国  1-2009', '1', 'http-get', '30000', '45');
# INSERT IGNORE INTO `task_management` VALUES ('eluosimv2', 'http://localhost:8090/douban/doubanMv?paramType=142201', '', '0 20 23 ? * 6', 'eluosimv2', '豆瓣  电影  俄罗斯  1-2020', '1', 'http-get', '30000', '50');
# INSERT IGNORE INTO `task_management` VALUES ('f9540c26f4ed49559e73748198b05685', 'http://localhost:8090/douban/doubanMv?paramType=012219', '', '0 0 3 ? * 6', 'f9540c26f4ed49559e73748198b05685', '豆瓣  电影  中国大陆  1-1979', '1', 'http-get', '30000', '7');
# INSERT IGNORE INTO `task_management` VALUES ('fgmv1', 'http://localhost:8090/douban/doubanMv?paramType=081101', '', '0 0 20 ? * 6', 'fgmv1', '豆瓣  电影  法国  2010-2020', '1', 'http-get', '30000', '41');
# INSERT IGNORE INTO `task_management` VALUES ('fgmv2', 'http://localhost:8090/douban/doubanMv?paramType=082212', '', '0 30 20 ? * 6', 'fgmv2', '豆瓣  电影  法国  1-2009', '1', 'http-get', '30000', '42');
# INSERT IGNORE INTO `task_management` VALUES ('hgmv1', 'http://localhost:8090/douban/doubanMv?paramType=060501', '', '0 30 12 ? * 6', 'hgmv1', '豆瓣  电影  韩国  2019-2020', '1', 'http-get', '30000', '26');
# INSERT IGNORE INTO `task_management` VALUES ('hgmv2', 'http://localhost:8090/douban/doubanMv?paramType=061106', '', '0 0 14 ? * 6', 'hgmv2', '豆瓣  电影  韩国  2000-2009', '1', 'http-get', '30000', '29');
# INSERT IGNORE INTO `task_management` VALUES ('hgmv3', 'http://localhost:8090/douban/doubanMv?paramType=061412', '', '0 30 13 ? * 6', 'hgmv3', '豆瓣  电影  韩国  2013-2015', '1', 'http-get', '30000', '28');
# INSERT IGNORE INTO `task_management` VALUES ('hgmv4', 'http://localhost:8090/douban/doubanMv?paramType=062217', '', '0 0 13 ? * 6', 'hgmv4', '豆瓣  电影  韩国  2016-2018', '1', 'http-get', '30000', '27');
# INSERT IGNORE INTO `task_management` VALUES ('jianadamv2', 'http://localhost:8090/douban/doubanMv?paramType=162201', '', '0 0 23 ? * 6', 'jianadamv2', '豆瓣  电影  加拿大  1-2020', '1', 'http-get', '30000', '49');
# INSERT IGNORE INTO `task_management` VALUES ('m1', 'http://localhost:8090/douban/doubanMv?paramType=020201', '', '0 0 9 ? * 6', 'm1', '豆瓣  电影  美国  2019-2020', '1', 'http-get', '30000', '19');
# INSERT IGNORE INTO `task_management` VALUES ('m2', 'http://localhost:8090/douban/doubanMv?paramType=021312', '', '0 0 11 ? * 6', 'm2', '豆瓣  电影  美国  2000-2009', '1', 'http-get', '30000', '23');
# INSERT IGNORE INTO `task_management` VALUES ('m3', 'http://localhost:8090/douban/doubanMv?paramType=020806', '', '0 0 10 ? * 6', 'm3', '豆瓣  电影  美国  2013-2015', '1', 'http-get', '30000', '21');
# INSERT IGNORE INTO `task_management` VALUES ('m4', 'http://localhost:8090/douban/doubanMv?paramType=020503', '', '0 30 9 ? * 6', 'm4', '豆瓣  电影  美国  2016-2018', '1', 'http-get', '30000', '20');
# INSERT IGNORE INTO `task_management` VALUES ('m5', 'http://localhost:8090/douban/doubanMv?paramType=021109', '', '0 30 10 ? * 6', 'm5', '豆瓣  电影  美国  2010-2012', '1', 'http-get', '30000', '22');
# INSERT IGNORE INTO `task_management` VALUES ('m6', 'http://localhost:8090/douban/doubanMv?paramType=021615', '', '0 30 11 ? * 6', 'm6', '豆瓣  电影  美国  1980-1999', '1', 'http-get', '30000', '24');
# INSERT IGNORE INTO `task_management` VALUES ('m7', 'http://localhost:8090/douban/doubanMv?paramType=022219', '', '0 0 12 ? * 6', 'm7', '豆瓣  电影  美国  1-1979', '1', 'http-get', '30000', '25');
# INSERT IGNORE INTO `task_management` VALUES ('rbmv1', 'http://localhost:8090/douban/doubanMv?paramType=050501', '', '0 30 15 ? * 6', 'rbmv1', '豆瓣  电影  日本  2019-2020', '1', 'http-get', '30000', '32');
# INSERT IGNORE INTO `task_management` VALUES ('rbmv2', 'http://localhost:8090/douban/doubanMv?paramType=051106', '', '0 0 17 ? * 6', 'rbmv2', '豆瓣  电影  日本  2000-2009', '1', 'http-get', '30000', '35');
# INSERT IGNORE INTO `task_management` VALUES ('rbmv3', 'http://localhost:8090/douban/doubanMv?paramType=051412', '', '0 30 16 ? * 6', 'rbmv3', '豆瓣  电影  日本  2013-2015', '1', 'http-get', '30000', '34');
# INSERT IGNORE INTO `task_management` VALUES ('rbmv4', 'http://localhost:8090/douban/doubanMv?paramType=052217', '', '0 0 16 ? * 6', 'rbmv4', '豆瓣  电影  日本  2016-2018', '1', 'http-get', '30000', '33');
# INSERT IGNORE INTO `task_management` VALUES ('ruidianmv2', 'http://localhost:8090/douban/doubanMv?paramType=192201', '', '0 0 18 ? * 6', 'ruidianmv2', '豆瓣  电影  瑞典  1-2020', '1', 'http-get', '30000', '37');
# INSERT IGNORE INTO `task_management` VALUES ('taiguomv2', 'http://localhost:8090/douban/doubanMv?paramType=132201', '', '0 30 19 ? * 6', 'taiguomv2', '豆瓣  电影  泰国  1-2020', '1', 'http-get', '30000', '40');
# INSERT IGNORE INTO `task_management` VALUES ('xbymv2', 'http://localhost:8090/douban/doubanMv?paramType=112201', '', '0 30 17 ? * 6', 'xbymv2', '豆瓣  电影  西班牙  1-2020', '1', 'http-get', '30000', '36');
# INSERT IGNORE INTO `task_management` VALUES ('ydlmv2', 'http://localhost:8090/douban/doubanMv?paramType=102201', '', '0 0 21 ? * 6', 'ydlmv2', '豆瓣  电影  意大利  1-2020', '1', 'http-get', '30000', '43');
# INSERT IGNORE INTO `task_management` VALUES ('ygmv1', 'http://localhost:8090/douban/doubanMv?paramType=071101', '', '0 0 15 ? * 6', 'ygmv1', '豆瓣  电影  英国  2010-2020', '1', 'http-get', '30000', '31');
# INSERT IGNORE INTO `task_management` VALUES ('ygmv2', 'http://localhost:8090/douban/doubanMv?paramType=072212', '', '0 30 14 ? * 6', 'ygmv2', '豆瓣  电影  英国  1-2009', '1', 'http-get', '30000', '30');
# INSERT IGNORE INTO `task_management` VALUES ('yilangmv2', 'http://localhost:8090/douban/doubanMv?paramType=152201', '', '0 59 23 ? * 6', 'yilangmv2', '豆瓣  电影  伊朗  1-2020', '1', 'http-get', '30000', '51');
# INSERT IGNORE INTO `task_management` VALUES ('yindumv1', 'http://localhost:8090/douban/doubanMv?paramType=121101', '', '0 20 22 ? * 6', 'yindumv1', '豆瓣  电影  印度  2010-2020', '1', 'http-get', '30000', '47');
# INSERT IGNORE INTO `task_management` VALUES ('yindumv2', 'http://localhost:8090/douban/doubanMv?paramType=122212', '', '0 40 22 ? * 6', 'yindumv2', '豆瓣  电影  印度  1-2009', '1', 'http-get', '30000', '48');
# INSERT IGNORE INTO `task_management` VALUES ('znhk1', 'http://localhost:8090/douban/doubanMv?paramType=030201', '', '0 30 3 ? * 6', 'znhk1', '豆瓣  电影  中国香港  2019-2020', '1', 'http-get', '30000', '8');
# INSERT IGNORE INTO `task_management` VALUES ('znhk2', 'http://localhost:8090/douban/doubanMv?paramType=031312', '', '0 30 5 ? * 6', 'znhk2', '豆瓣  电影  中国香港  2000-2009', '1', 'http-get', '30000', '12');
# INSERT IGNORE INTO `task_management` VALUES ('znhk3', 'http://localhost:8090/douban/doubanMv?paramType=030806', '', '0 30 4 ? * 6', 'znhk3', '豆瓣  电影  中国香港  2013-2015', '1', 'http-get', '30000', '10');
# INSERT IGNORE INTO `task_management` VALUES ('znhk4', 'http://localhost:8090/douban/doubanMv?paramType=030503', '', '0 0 4 ? * 6', 'znhk4', '豆瓣  电影  中国香港  2016-2018', '1', 'http-get', '30000', '9');
# INSERT IGNORE INTO `task_management` VALUES ('znhk5', 'http://localhost:8090/douban/doubanMv?paramType=031109', '', '0 0 5 ? * 6', 'znhk5', '豆瓣  电影  中国香港  2010-2012', '1', 'http-get', '30000', '11');
# INSERT IGNORE INTO `task_management` VALUES ('znhk6', 'http://localhost:8090/douban/doubanMv?paramType=031615', '', '0 0 6 ? * 6', 'znhk6', '豆瓣  电影  中国香港  1980-1999', '1', 'http-get', '30000', '13');
# INSERT IGNORE INTO `task_management` VALUES ('znhk7', 'http://localhost:8090/douban/doubanMv?paramType=032219', '', '0 30 6 ? * 6', 'znhk7', '豆瓣  电影  中国香港  1-1979', '1', 'http-get', '30000', '14');
# INSERT IGNORE INTO `task_management` VALUES ('zntw1', 'http://localhost:8090/douban/doubanMv?paramType=040501', '', '0 0 7 ? * 6', 'zntw1', '豆瓣  电影  中国台湾  2019-2020', '1', 'http-get', '30000', '15');
# INSERT IGNORE INTO `task_management` VALUES ('zntw2', 'http://localhost:8090/douban/doubanMv?paramType=041106', '', '0 30 8 ? * 6', 'zntw2', '豆瓣  电影  中国台湾  2000-2009', '1', 'http-get', '30000', '18');
# INSERT IGNORE INTO `task_management` VALUES ('zntw3', 'http://localhost:8090/douban/doubanMv?paramType=041412', '', '0 0 8 ? * 6', 'zntw3', '豆瓣  电影  中国台湾  2013-2015', '1', 'http-get', '30000', '17');
# INSERT IGNORE INTO `task_management` VALUES ('zntw4', 'http://localhost:8090/douban/doubanMv?paramType=042217', '', '0 30 7 ? * 6', 'zntw4', '豆瓣  电影  中国台湾  2016-2018', '1', 'http-get', '30000', '16');
INSERT IGNORE INTO `task_management` VALUES ('33b0e4e99eec4fafa266b69770cf76dc', 'http://localhost:8090/douban/doubanMv?paramType=010201', '', '0 0 0 * * ?', '33b0e4e99eec4fafa266b69770cf76dc', '豆瓣  电影  中国大陆  2019-2020', '1', 'http-get', '30000', '1');
INSERT IGNORE INTO `task_management` VALUES ('441672e3088f4ecb8f6ea6118197d21b', 'http://localhost:8090/douban/doubanMv?paramType=011312', '', '0 0 2 * * ?', '441672e3088f4ecb8f6ea6118197d21b', '豆瓣  电影  中国大陆  2000-2009', '1', 'http-get', '30000', '5');
INSERT IGNORE INTO `task_management` VALUES ('54b03afbb34948bb8bea024827134abc', 'http://localhost:8090/douban/doubanMv?paramType=010806', '', '0 0 1 * * ?', '54b03afbb34948bb8bea024827134abc', '豆瓣  电影  中国大陆  2013-2015', '1', 'http-get', '30000', '3');
INSERT IGNORE INTO `task_management` VALUES ('67222da9e0ef46f19d38f47c1688bd7c', 'http://localhost:8090/douban/doubanMv?paramType=010503', '', '0 30 0 * * ?', '67222da9e0ef46f19d38f47c1688bd7c', '豆瓣  电影  中国大陆  2016-2018', '1', 'http-get', '30000', '2');
INSERT IGNORE INTO `task_management` VALUES ('aerlan2', 'http://localhost:8090/douban/doubanMv?paramType=182201', '', '0 30 18 * * ?', 'aerlan2', '豆瓣  电影  爱尔兰  1-2020', '1', 'http-get', '30000', '38');
INSERT IGNORE INTO `task_management` VALUES ('aodaliyamv2', 'http://localhost:8090/douban/doubanMv?paramType=172201', '', '0 0 19 * * ?', 'aodaliyamv2', '豆瓣  电影  澳大利亚  1-2020', '1', 'http-get', '30000', '39');
INSERT IGNORE INTO `task_management` VALUES ('baximv2', 'http://localhost:8090/douban/doubanMv?paramType=202201', '', '0 0 22 * * ?', 'baximv2', '豆瓣  电影  巴西  1-2020', '1', 'http-get', '30000', '46');
INSERT IGNORE INTO `task_management` VALUES ('c67200fc74a74bfa99b042370177690e', 'http://localhost:8090/douban/doubanMv?paramType=011109', '', '0 30 1 * * ?', 'c67200fc74a74bfa99b042370177690e', '豆瓣  电影  中国大陆  2010-2012', '1', 'http-get', '30000', '4');
INSERT IGNORE INTO `task_management` VALUES ('d5c831a2ea42401a8056d47546f99ba8', 'http://localhost:8090/douban/doubanMv?paramType=011615', '', '0 30 2 * * ?', 'd5c831a2ea42401a8056d47546f99ba8', '豆瓣  电影  中国大陆  1980-1999', '1', 'http-get', '30000', '6');
INSERT IGNORE INTO `task_management` VALUES ('danmaimv2', 'http://localhost:8090/douban/doubanMv?paramType=212201', '', '0 30 23 * * ?', 'danmaimv2', '豆瓣  电影  丹麦  1-2020', '1', 'http-get', '30000', '51');
INSERT IGNORE INTO `task_management` VALUES ('dgmv1', 'http://localhost:8090/douban/doubanMv?paramType=091101', '', '0 20 21 * * ?', 'dgmv1', '豆瓣  电影  德国  2010-2020', '1', 'http-get', '30000', '44');
INSERT IGNORE INTO `task_management` VALUES ('dgmv2', 'http://localhost:8090/douban/doubanMv?paramType=092212', '', '0 40 21 * * ?', 'dgmv2', '豆瓣  电影  德国  1-2009', '1', 'http-get', '30000', '45');
INSERT IGNORE INTO `task_management` VALUES ('eluosimv2', 'http://localhost:8090/douban/doubanMv?paramType=142201', '', '0 15 23 * * ?', 'eluosimv2', '豆瓣  电影  俄罗斯  1-2020', '1', 'http-get', '30000', '50');
INSERT IGNORE INTO `task_management` VALUES ('f9540c26f4ed49559e73748198b05685', 'http://localhost:8090/douban/doubanMv?paramType=012219', '', '0 0 3 * * ?', 'f9540c26f4ed49559e73748198b05685', '豆瓣  电影  中国大陆  1-1979', '1', 'http-get', '30000', '7');
INSERT IGNORE INTO `task_management` VALUES ('fgmv1', 'http://localhost:8090/douban/doubanMv?paramType=081101', '', '0 0 20 * * ?', 'fgmv1', '豆瓣  电影  法国  2010-2020', '1', 'http-get', '30000', '41');
INSERT IGNORE INTO `task_management` VALUES ('fgmv2', 'http://localhost:8090/douban/doubanMv?paramType=082212', '', '0 30 20 * * ?', 'fgmv2', '豆瓣  电影  法国  1-2009', '1', 'http-get', '30000', '42');
INSERT IGNORE INTO `task_management` VALUES ('hgmv1', 'http://localhost:8090/douban/doubanMv?paramType=060501', '', '0 30 12 * * ?', 'hgmv1', '豆瓣  电影  韩国  2019-2020', '1', 'http-get', '30000', '26');
INSERT IGNORE INTO `task_management` VALUES ('hgmv2', 'http://localhost:8090/douban/doubanMv?paramType=061106', '', '0 0 14 * * ?', 'hgmv2', '豆瓣  电影  韩国  2000-2009', '1', 'http-get', '30000', '29');
INSERT IGNORE INTO `task_management` VALUES ('hgmv3', 'http://localhost:8090/douban/doubanMv?paramType=061412', '', '0 30 13 * * ?', 'hgmv3', '豆瓣  电影  韩国  2013-2015', '1', 'http-get', '30000', '28');
INSERT IGNORE INTO `task_management` VALUES ('hgmv4', 'http://localhost:8090/douban/doubanMv?paramType=062217', '', '0 0 13 * * ?', 'hgmv4', '豆瓣  电影  韩国  2016-2018', '1', 'http-get', '30000', '27');
INSERT IGNORE INTO `task_management` VALUES ('jianadamv2', 'http://localhost:8090/douban/doubanMv?paramType=162201', '', '0 0 23 * * ?', 'jianadamv2', '豆瓣  电影  加拿大  1-2020', '1', 'http-get', '30000', '49');
INSERT IGNORE INTO `task_management` VALUES ('m1', 'http://localhost:8090/douban/doubanMv?paramType=020201', '', '0 0 9 * * ?', 'm1', '豆瓣  电影  美国  2019-2020', '1', 'http-get', '30000', '19');
INSERT IGNORE INTO `task_management` VALUES ('m2', 'http://localhost:8090/douban/doubanMv?paramType=021312', '', '0 0 11 * * ?', 'm2', '豆瓣  电影  美国  2000-2009', '1', 'http-get', '30000', '23');
INSERT IGNORE INTO `task_management` VALUES ('m3', 'http://localhost:8090/douban/doubanMv?paramType=020806', '', '0 0 10 * * ?', 'm3', '豆瓣  电影  美国  2013-2015', '1', 'http-get', '30000', '21');
INSERT IGNORE INTO `task_management` VALUES ('m4', 'http://localhost:8090/douban/doubanMv?paramType=020503', '', '0 30 9 * * ?', 'm4', '豆瓣  电影  美国  2016-2018', '1', 'http-get', '30000', '20');
INSERT IGNORE INTO `task_management` VALUES ('m5', 'http://localhost:8090/douban/doubanMv?paramType=021109', '', '0 30 10 * * ?', 'm5', '豆瓣  电影  美国  2010-2012', '1', 'http-get', '30000', '22');
INSERT IGNORE INTO `task_management` VALUES ('m6', 'http://localhost:8090/douban/doubanMv?paramType=021615', '', '0 30 11 * * ?', 'm6', '豆瓣  电影  美国  1980-1999', '1', 'http-get', '30000', '24');
INSERT IGNORE INTO `task_management` VALUES ('m7', 'http://localhost:8090/douban/doubanMv?paramType=022219', '', '0 0 12 * * ?', 'm7', '豆瓣  电影  美国  1-1979', '1', 'http-get', '30000', '25');
INSERT IGNORE INTO `task_management` VALUES ('rbmv1', 'http://localhost:8090/douban/doubanMv?paramType=050501', '', '0 30 15 * * ?', 'rbmv1', '豆瓣  电影  日本  2019-2020', '1', 'http-get', '30000', '32');
INSERT IGNORE INTO `task_management` VALUES ('rbmv2', 'http://localhost:8090/douban/doubanMv?paramType=051106', '', '0 0 17 * * ?', 'rbmv2', '豆瓣  电影  日本  2000-2009', '1', 'http-get', '30000', '35');
INSERT IGNORE INTO `task_management` VALUES ('rbmv3', 'http://localhost:8090/douban/doubanMv?paramType=051412', '', '0 30 16 * * ?', 'rbmv3', '豆瓣  电影  日本  2013-2015', '1', 'http-get', '30000', '34');
INSERT IGNORE INTO `task_management` VALUES ('rbmv4', 'http://localhost:8090/douban/doubanMv?paramType=052217', '', '0 0 16 * * ?', 'rbmv4', '豆瓣  电影  日本  2016-2018', '1', 'http-get', '30000', '33');
INSERT IGNORE INTO `task_management` VALUES ('ruidianmv2', 'http://localhost:8090/douban/doubanMv?paramType=192201', '', '0 0 18 * * ?', 'ruidianmv2', '豆瓣  电影  瑞典  1-2020', '1', 'http-get', '30000', '37');
INSERT IGNORE INTO `task_management` VALUES ('taiguomv2', 'http://localhost:8090/douban/doubanMv?paramType=132201', '', '0 30 19 * * ?', 'taiguomv2', '豆瓣  电影  泰国  1-2020', '1', 'http-get', '30000', '40');
INSERT IGNORE INTO `task_management` VALUES ('xbymv2', 'http://localhost:8090/douban/doubanMv?paramType=112201', '', '0 30 17 * * ?', 'xbymv2', '豆瓣  电影  西班牙  1-2020', '1', 'http-get', '30000', '36');
INSERT IGNORE INTO `task_management` VALUES ('ydlmv2', 'http://localhost:8090/douban/doubanMv?paramType=102201', '', '0 0 21 * * ?', 'ydlmv2', '豆瓣  电影  意大利  1-2020', '1', 'http-get', '30000', '43');
INSERT IGNORE INTO `task_management` VALUES ('ygmv1', 'http://localhost:8090/douban/doubanMv?paramType=071101', '', '0 0 15 * * ?', 'ygmv1', '豆瓣  电影  英国  2010-2020', '1', 'http-get', '30000', '31');
INSERT IGNORE INTO `task_management` VALUES ('ygmv2', 'http://localhost:8090/douban/doubanMv?paramType=072212', '', '0 30 14 * * ?', 'ygmv2', '豆瓣  电影  英国  1-2009', '1', 'http-get', '30000', '30');
INSERT IGNORE INTO `task_management` VALUES ('yilangmv2', 'http://localhost:8090/douban/doubanMv?paramType=152201', '', '0 45 23 * * ?', 'yilangmv2', '豆瓣  电影  伊朗  1-2020', '1', 'http-get', '30000', '51');
INSERT IGNORE INTO `task_management` VALUES ('yindumv1', 'http://localhost:8090/douban/doubanMv?paramType=121101', '', '0 20 22 * * ?', 'yindumv1', '豆瓣  电影  印度  2010-2020', '1', 'http-get', '30000', '47');
INSERT IGNORE INTO `task_management` VALUES ('yindumv2', 'http://localhost:8090/douban/doubanMv?paramType=122212', '', '0 40 22 * * ?', 'yindumv2', '豆瓣  电影  印度  1-2009', '1', 'http-get', '30000', '48');
INSERT IGNORE INTO `task_management` VALUES ('znhk1', 'http://localhost:8090/douban/doubanMv?paramType=030201', '', '0 30 3 * * ?', 'znhk1', '豆瓣  电影  中国香港  2019-2020', '1', 'http-get', '30000', '8');
INSERT IGNORE INTO `task_management` VALUES ('znhk2', 'http://localhost:8090/douban/doubanMv?paramType=031312', '', '0 30 5 * * ?', 'znhk2', '豆瓣  电影  中国香港  2000-2009', '1', 'http-get', '30000', '12');
INSERT IGNORE INTO `task_management` VALUES ('znhk3', 'http://localhost:8090/douban/doubanMv?paramType=030806', '', '0 30 4 * * ?', 'znhk3', '豆瓣  电影  中国香港  2013-2015', '1', 'http-get', '30000', '10');
INSERT IGNORE INTO `task_management` VALUES ('znhk4', 'http://localhost:8090/douban/doubanMv?paramType=030503', '', '0 0 4 * * ?', 'znhk4', '豆瓣  电影  中国香港  2016-2018', '1', 'http-get', '30000', '9');
INSERT IGNORE INTO `task_management` VALUES ('znhk5', 'http://localhost:8090/douban/doubanMv?paramType=031109', '', '0 0 5 * * ?', 'znhk5', '豆瓣  电影  中国香港  2010-2012', '1', 'http-get', '30000', '11');
INSERT IGNORE INTO `task_management` VALUES ('znhk6', 'http://localhost:8090/douban/doubanMv?paramType=031615', '', '0 0 6 * * ?', 'znhk6', '豆瓣  电影  中国香港  1980-1999', '1', 'http-get', '30000', '13');
INSERT IGNORE INTO `task_management` VALUES ('znhk7', 'http://localhost:8090/douban/doubanMv?paramType=032219', '', '0 30 6 * * ?', 'znhk7', '豆瓣  电影  中国香港  1-1979', '1', 'http-get', '30000', '14');
INSERT IGNORE INTO `task_management` VALUES ('zntw1', 'http://localhost:8090/douban/doubanMv?paramType=040501', '', '0 0 7 * * ?', 'zntw1', '豆瓣  电影  中国台湾  2019-2020', '1', 'http-get', '30000', '15');
INSERT IGNORE INTO `task_management` VALUES ('zntw2', 'http://localhost:8090/douban/doubanMv?paramType=041106', '', '0 30 8 * * ?', 'zntw2', '豆瓣  电影  中国台湾  2000-2009', '1', 'http-get', '30000', '18');
INSERT IGNORE INTO `task_management` VALUES ('zntw3', 'http://localhost:8090/douban/doubanMv?paramType=041412', '', '0 0 8 * * ?', 'zntw3', '豆瓣  电影  中国台湾  2013-2015', '1', 'http-get', '30000', '17');
INSERT IGNORE INTO `task_management` VALUES ('zntw4', 'http://localhost:8090/douban/doubanMv?paramType=042217', '', '0 30 7 * * ?', 'zntw4', '豆瓣  电影  中国台湾  2016-2018', '1', 'http-get', '30000', '16');
