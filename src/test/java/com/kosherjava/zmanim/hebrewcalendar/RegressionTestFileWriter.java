package com.kosherjava.zmanim.hebrewcalendar;

import com.kosherjava.zmanim.ComprehensiveZmanimCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RegressionTestFileWriter {
    public static void main(String[] args) throws IOException {
        //generates file with all the Jewish dates and times from 1/1/1 to 1/1/9999
        LocalDate start = LocalDate.of(1, 1, 1);
        LocalDate end = LocalDate.of(9999, 1, 1);
        LocalDate current = start;
        JewishCalendar cal = new JewishCalendar(current);
        GregorianCalendar gregorian = new GregorianCalendar(current.getYear(), current.getMonthValue() - 1, current.getDayOfMonth());
        JewishDate date = new JewishDate(current);
        ComprehensiveZmanimCalendar zcal = new ComprehensiveZmanimCalendar(new GeoLocation("Lakewood, NJ", 40.096, -74.222, 29.02, TimeZone.getTimeZone("America/New_York")));
        List<FullCalendar> calendars = new ArrayList<>();
        List<FullZmanim> zmanim = new ArrayList<>();

//            cal.setUseModernHolidays();
//            cal.setInIsrael();
//            cal.setIsMukafChoma();
        while (current.isBefore(end)) {
            //TODO work in progress:
			calendars.add(new FullCalendar(current, date, cal.getYomTovIndex(), cal.getDafYomiBavli(),
					cal.getDafYomiYerushalmi(), cal.isIsruChag(), cal.isBirkasHachamah(), cal.getParshah(),
					cal.getUpcomingParshah(), cal.getSpecialShabbos(), cal.isYomTov(), cal.isYomTovAssurBemelacha(),
					cal.isAssurBemelacha(), cal.hasCandleLighting(), cal.isTomorrowShabbosOrYomTov(),
					cal.isErevYomTovSheni(), cal.isAseresYemeiTeshuva(), cal.isPesach(), cal.isCholHamoedPesach(),
					cal.isShavuos(), cal.isRoshHashana(), cal.isYomKippur(), cal.isSuccos(), cal.isHoshanaRabba(),
					cal.isShminiAtzeres(), cal.isSimchasTorah(), cal.isCholHamoedSuccos(), cal.isCholHamoed(),
					cal.isErevYomTov(), cal.isErevRoshChodesh(), cal.isYomKippurKatan(), cal.isBeHaB(), cal.isTaanis(),
					cal.isTaanisBechoros(), cal.getDayOfChanukah(), cal.isChanukah(), cal.isPurim(),
					cal.isRoshChodesh(), cal.isMacharChodesh(), cal.isShabbosMevorchim(), cal.getDayOfOmer(),
					cal.isTishaBav(), cal.getMolad(), cal.getMoladAsDate(), cal.getTchilasZmanKidushLevana3Days(),
					cal.getTchilasZmanKidushLevana7Days(), cal.getSofZmanKidushLevanaBetweenMoldos(),
					cal.getSofZmanKidushLevana15Days(), cal.getTekufasTishreiElapsedDays()));
			
			zmanim.add(new FullZmanim(zcal.getShaahZmanis19Point8Degrees(), zcal.getShaahZmanis18Degrees(),
					zcal.getShaahZmanis26Degrees(), zcal.getShaahZmanis16Point1Degrees(),
					zcal.getShaahZmanis60Minutes(), zcal.getShaahZmanis72Minutes(),
					zcal.getShaahZmanis72MinutesZmanis(), zcal.getShaahZmanis90Minutes(),
					zcal.getShaahZmanis90MinutesZmanis(), zcal.getShaahZmanis96MinutesZmanis(),
					zcal.getShaahZmanisAteretTorah(), zcal.getShaahZmanisAlos16Point1ToTzais3Point8(),
					zcal.getShaahZmanisAlos16Point1ToTzais3Point7(), zcal.getShaahZmanis96Minutes(),
					zcal.getShaahZmanis120Minutes(), zcal.getShaahZmanis120MinutesZmanis(),
					zcal.getPlagHamincha120MinutesZmanis(), zcal.getPlagHamincha120Minutes(), zcal.getAlos60(),
					zcal.getAlos72Zmanis(), zcal.getAlos96(), zcal.getAlos90Zmanis(), zcal.getAlos96Zmanis(),
					zcal.getAlos90(), zcal.getAlos120(), zcal.getAlos120Zmanis(), zcal.getAlos26Degrees(),
					zcal.getAlos18Degrees(), zcal.getAlos19Degrees(), zcal.getAlos19Point8Degrees(),
					zcal.getAlos16Point1Degrees(), zcal.getMisheyakir11Point5Degrees(), zcal.getMisheyakir11Degrees(),
					zcal.getMisheyakir10Point2Degrees(), zcal.getMisheyakir7Point65Degrees(),
					zcal.getMisheyakir9Point5Degrees(), zcal.getSunrise(), zcal.getSeaLevelSunrise(),
					zcal.getSofZmanShmaMGA16Point1Degrees(), zcal.getSofZmanShmaMGA72Minutes(),
					zcal.getSofZmanShmaMGA72MinutesZmanis(), zcal.getSofZmanShmaMGA90Minutes(),
					zcal.getSofZmanShmaMGA90MinutesZmanis(), zcal.getSofZmanShmaMGA96Minutes(),
					zcal.getSofZmanShmaMGA96MinutesZmanis(), zcal.getSofZmanShma3HoursBeforeChatzos(),
					zcal.getSofZmanShmaMGA120Minutes(), zcal.getSofZmanShmaAlos16Point1ToSunset(),
					zcal.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees(),
					zcal.getSofZmanTfilaMGA19Point8Degrees(), zcal.getSofZmanTfilaMGA16Point1Degrees(),
					zcal.getSofZmanTfilaMGA18Degrees(), zcal.getSofZmanTfilaMGA72Minutes(),
					zcal.getSofZmanTfilaMGA72MinutesZmanis(), zcal.getSofZmanTfilaMGA90Minutes(),
					zcal.getSofZmanTfilaMGA90MinutesZmanis(), zcal.getSofZmanTfilaMGA96Minutes(),
					zcal.getSofZmanTfilaMGA96MinutesZmanis(), zcal.getSofZmanTfilaMGA120Minutes(),
					zcal.getSofZmanTfila2HoursBeforeChatzos(), zcal.getMinchaGedola30Minutes(),
					zcal.getMinchaGedola72Minutes(), zcal.getMinchaGedola16Point1Degrees(),
					zcal.getMinchaGedolaAhavatShalom(), zcal.getMinchaGedolaGreaterThan30(),
					zcal.getMinchaKetana16Point1Degrees(), zcal.getMinchaKetanaAhavatShalom(),
					zcal.getMinchaKetana72Minutes(), zcal.getPlagHamincha60Minutes(), zcal.getPlagHamincha72Minutes(),
					zcal.getPlagHamincha90Minutes(), zcal.getPlagHamincha96Minutes(),
					zcal.getPlagHamincha96MinutesZmanis(), zcal.getPlagHamincha90MinutesZmanis(),
					zcal.getPlagHamincha72MinutesZmanis(), zcal.getPlagHamincha16Point1Degrees(),
					zcal.getPlagHamincha19Point8Degrees(), zcal.getPlagHamincha26Degrees(),
					zcal.getPlagHamincha18Degrees(), zcal.getPlagAlosToSunset(),
					zcal.getPlagAlos16Point1ToTzaisGeonim7Point083Degrees(), zcal.getPlagAhavatShalom(),
					zcal.getBainHashmashosRT13Point24Degrees(), zcal.getBainHashmashosRT58Point5Minutes(),
					zcal.getBainHashmashosRT13Point5MinutesBefore7Point083Degrees(), zcal.getBainHashmashosRT2Stars(),
					zcal.getBainHashmashosYereim18Minutes(), zcal.getBainHashmashosYereim3Point05Degrees(),
					zcal.getBainHashmashosYereim16Point875Minutes(), zcal.getBainHashmashosYereim2Point8Degrees(),
					zcal.getBainHashmashosYereim13Point5Minutes(), zcal.getBainHashmashosYereim2Point1Degrees(),
					zcal.getTzaisGeonim3Point7Degrees(), zcal.getTzaisGeonim3Point8Degrees(),
					zcal.getTzaisGeonim5Point95Degrees(),zcal.getTzaisGeonim4Point61Degrees(),
					zcal.getTzaisGeonim4Point37Degrees(), zcal.getTzaisGeonim5Point88Degrees(),
					zcal.getTzaisGeonim4Point8Degrees(), zcal.getTzaisGeonim6Point45Degrees(),
					zcal.getTzaisGeonim7Point083Degrees(), zcal.getTzaisGeonim7Point67Degrees(),
					zcal.getTzaisGeonim8Point5Degrees(), zcal.getTzaisGeonim9Point3Degrees(),
					zcal.getTzaisGeonim9Point75Degrees(), zcal.getTzais60(), zcal.getTzaisAteretTorah(),
					zcal.getSofZmanShmaAteretTorah(), zcal.getSofZmanTfilaAteretTorah(),
					zcal.getMinchaGedolaAteretTorah(), zcal.getMinchaKetanaAteretTorah(),
					zcal.getPlagHaminchaAteretTorah(), zcal.getTzais72Zmanis(), zcal.getTzais90Zmanis(),
					zcal.getTzais96Zmanis(), zcal.getTzais90(), zcal.getTzais120(), zcal.getTzais120Zmanis(),
					zcal.getTzais16Point1Degrees(), zcal.getTzais26Degrees(), zcal.getTzais18Degrees(),
					zcal.getTzais19Point8Degrees(), zcal.getTzais96(), zcal.getFixedLocalChatzos(),
					zcal.getSofZmanKidushLevanaBetweenMoldos(), zcal.getSofZmanKidushLevana15Days(),
					zcal.getTchilasZmanKidushLevana3Days(), zcal.getZmanMolad(), zcal.getTchilasZmanKidushLevana7Days(),
					zcal.getSofZmanAchilasChametzGRA(), zcal.getSofZmanAchilasChametzMGA72Minutes(),
					zcal.getSofZmanAchilasChametzMGA16Point1Degrees(), zcal.getSofZmanBiurChametzGRA(),
					zcal.getSofZmanBiurChametzMGA72Minutes(), zcal.getSofZmanBiurChametzMGA16Point1Degrees(),
					zcal.getSolarMidnight(), zcal.getShaahZmanisBaalHatanya(), zcal.getAlosBaalHatanya(),
					zcal.getSofZmanShmaBaalHatanya(), zcal.getSofZmanTfilaBaalHatanya(),
					zcal.getSofZmanAchilasChametzBaalHatanya(), zcal.getSofZmanBiurChametzBaalHatanya(),
					zcal.getMinchaGedolaBaalHatanya(), zcal.getMinchaGedolaBaalHatanyaGreaterThan30(),
					zcal.getMinchaKetanaBaalHatanya(), zcal.getPlagHaminchaBaalHatanya(), zcal.getTzaisBaalHatanya(),
					zcal.getSofZmanShmaMGA18DegreesToFixedLocalChatzos(),
					zcal.getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos(),
					zcal.getSofZmanShmaMGA90MinutesToFixedLocalChatzos(),
					zcal.getSofZmanShmaMGA72MinutesToFixedLocalChatzos(),
					zcal.getSofZmanShmaGRASunriseToFixedLocalChatzos(),
					zcal.getSofZmanTfilaGRASunriseToFixedLocalChatzos(),
					zcal.getMinchaGedolaGRAFixedLocalChatzos30Minutes(),
					zcal.getMinchaKetanaGRAFixedLocalChatzosToSunset(),
					zcal.getPlagHaminchaGRAFixedLocalChatzosToSunset(), zcal.getTzais50(),
					zcal.getSamuchLeMinchaKetanaGRA(), zcal.getSamuchLeMinchaKetana16Point1Degrees(),
					zcal.getSamuchLeMinchaKetana72Minutes()));
            //deprecated
            /*cal.isVeseinTalUmatarStartDate();
            cal.isVeseinTalUmatarStartingTonight();
            cal.isVeseinTalUmatarRecited();
            cal.isVeseinBerachaRecited();
            cal.isMashivHaruachStartDate();
            cal.isMashivHaruachEndDate();
            cal.isMashivHaruachRecited();
            cal.isMoridHatalRecited();*/

            current = current.plusDays(1L);
            gregorian.roll(Calendar.DATE, true);
            date.forward(Calendar.DATE, 1);
            cal.setDate(current);
            zcal.setCalendar(gregorian);
        }
        //write calendars to file:

        File calendarOutput = new File("lakewood_calendar.csv");
        BufferedWriter calendarWriter = new BufferedWriter(new FileWriter(calendarOutput));
        calendarWriter.write(FullCalendar.fields);
        calendarWriter.newLine();
        for (FullCalendar calendar : calendars) {
            calendarWriter.write(calendar.toString());
            calendarWriter.newLine();
        }
        calendarWriter.close();
        
        File zmanimOutput = new File("lakewood_zmanim.csv");
        BufferedWriter zmanimWriter = new BufferedWriter(new FileWriter(zmanimOutput));
        zmanimWriter.write(FullZmanim.fields);
        zmanimWriter.newLine();
        for (FullZmanim zman : zmanim) {
            zmanimWriter.write(zman.toString());
            zmanimWriter.newLine();
        }
        zmanimWriter.close();
    }

    static class FullZmanim {
		public static final String fields = "getShaahZmanis19Point8Degrees,getShaahZmanis18Degrees,getShaahZmanis26Degrees,getShaahZmanis16Point1Degrees,getShaahZmanis60Minutes,getShaahZmanis72Minutes,getShaahZmanis72MinutesZmanis,getShaahZmanis90Minutes,getShaahZmanis90MinutesZmanis,getShaahZmanis96MinutesZmanis,getShaahZmanisAteretTorah,getShaahZmanisAlos16Point1ToTzais3Point8,getShaahZmanisAlos16Point1ToTzais3Point7,getShaahZmanis96Minutes,getShaahZmanis120Minutes,getShaahZmanis120MinutesZmanis,getPlagHamincha120MinutesZmanis,getPlagHamincha120Minutes,getAlos60,getAlos72Zmanis,getAlos96,getAlos90Zmanis,getAlos96Zmanis,getAlos90,getAlos120,getAlos120Zmanis,getAlos26Degrees,getAlos18Degrees,getAlos19Degrees,getAlos19Point8Degrees,getAlos16Point1Degrees,getMisheyakir11Point5Degrees,getMisheyakir11Degrees,getMisheyakir10Point2Degrees,getMisheyakir7Point65Degrees,getMisheyakir9Point5Degrees,getSofZmanShmaMGA19Point8Degrees,getSofZmanShmaMGA16Point1Degrees,getSofZmanShmaMGA18Degrees,getSofZmanShmaMGA72Minutes,getSofZmanShmaMGA72MinutesZmanis,getSofZmanShmaMGA90Minutes,getSofZmanShmaMGA90MinutesZmanis,getSofZmanShmaMGA96Minutes,getSofZmanShmaMGA96MinutesZmanis,getSofZmanShma3HoursBeforeChatzos,getSofZmanShmaMGA120Minutes,getSofZmanShmaAlos16Point1ToSunset,getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees,getSofZmanTfilaMGA19Point8Degrees,getSofZmanTfilaMGA16Point1Degrees,getSofZmanTfilaMGA18Degrees,getSofZmanTfilaMGA72Minutes,getSofZmanTfilaMGA72MinutesZmanis,getSofZmanTfilaMGA90Minutes,getSofZmanTfilaMGA90MinutesZmanis,getSofZmanTfilaMGA96Minutes,getSofZmanTfilaMGA96MinutesZmanis,getSofZmanTfilaMGA120Minutes,getSofZmanTfila2HoursBeforeChatzos,getMinchaGedola30Minutes,getMinchaGedola72Minutes,getMinchaGedola16Point1Degrees,getMinchaGedolaAhavatShalom,getMinchaGedolaGreaterThan30,getMinchaKetana16Point1Degrees,getMinchaKetanaAhavatShalom,getMinchaKetana72Minutes,getPlagHamincha60Minutes,getPlagHamincha72Minutes,getPlagHamincha90Minutes,getPlagHamincha96Minutes,getPlagHamincha96MinutesZmanis,getPlagHamincha90MinutesZmanis,getPlagHamincha72MinutesZmanis,getPlagHamincha16Point1Degrees,getPlagHamincha19Point8Degrees,getPlagHamincha26Degrees,getPlagHamincha18Degrees,getPlagAlosToSunset,getPlagAlos16Point1ToTzaisGeonim7Point083Degrees,getPlagAhavatShalom,getBainHashmashosRT58Point5Minutes,getBainHashmashosRT13Point5MinutesBefore7Point083Degrees,getBainHashmashosRT2Stars,getBainHashmashosYereim18Minutes,getBainHashmashosYereim3Point05Degrees,getBainHashmashosYereim16Point875Minutes,getBainHashmashosYereim2Point8Degrees,getBainHashmashosYereim13Point5Minutes,getBainHashmashosYereim2Point1Degrees,getTzaisGeonim3Point7Degrees,getTzaisGeonim3Point8Degrees,getTzaisGeonim5Point95Degrees,getTzaisGeonim4Point61Degrees,getTzaisGeonim4Point37Degrees,getTzaisGeonim5Point88Degrees,getTzaisGeonim4Point8Degrees,getTzaisGeonim6Point45Degrees,getTzaisGeonim7Point083Degrees,getTzaisGeonim7Point67Degrees,getTzaisGeonim8Point5Degrees,getTzaisGeonim9Point3Degrees,getTzaisGeonim9Point75Degrees,getTzais60,getTzaisAteretTorah,getSofZmanShmaAteretTorah,getSofZmanTfilahAteretTorah,getMinchaGedolaAteretTorah,getMinchaKetanaAteretTorah,getPlagHaminchaAteretTorah,getTzais72Zmanis,getTzais90Zmanis,getTzais96Zmanis,getTzais90,getTzais120,getTzais120Zmanis,getTzais16Point1Degrees,getTzais26Degrees,getTzais18Degrees,getTzais19Point8Degrees,getTzais96,getFixedLocalChatzos,getSofZmanKidushLevanaBetweenMoldos,getSofZmanKidushLevana15Days,getTchilasZmanKidushLevana3Days,getZmanMolad,getTchilasZmanKidushLevana7Days,getSofZmanAchilasChametzGRA,getSofZmanAchilasChametzMGA72Minutes,getSofZmanAchilasChametzMGA16Point1Degrees,getSofZmanBiurChametzGRA,getSofZmanBiurChametzMGA72Minutes,getSofZmanBiurChametzMGA16Point1Degrees,getSolarMidnight,getShaahZmanisBaalHatanya,getAlosBaalHatanya,getSofZmanShmaBaalHatanya,getSofZmanTfilaBaalHatanya,getSofZmanAchilasChametzBaalHatanya,getSofZmanBiurChametzBaalHatanya,getMinchaGedolaBaalHatanya,getMinchaGedolaBaalHatanyaGreaterThan30,getMinchaKetanaBaalHatanya,getPlagHaminchaBaalHatanya,getTzaisBaalHatanya,getSofZmanShmaMGA18DegreesToFixedLocalChatzos,getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos,getSofZmanShmaMGA90MinutesToFixedLocalChatzos,getSofZmanShmaMGA72MinutesToFixedLocalChatzos,getSofZmanShmaGRASunriseToFixedLocalChatzos,getSofZmanTfilaGRASunriseToFixedLocalChatzos,getMinchaGedolaGRAFixedLocalChatzos30Minutes,getMinchaKetanaGRAFixedLocalChatzosToSunset,getPlagHaminchaGRAFixedLocalChatzosToSunset,getTzais50,getSamuchLeMinchaKetanaGRA,getSamuchLeMinchaKetana16Point1Degrees,getSamuchLeMinchaKetana72Minutes";

        @Override
        public String toString() {
            return new StringJoiner(",")
                    .add(Long.toString(getShaahZmanis19Point8Degrees))
                    .add(Long.toString(getShaahZmanis18Degrees))
                    .add(Long.toString(getShaahZmanis26Degrees))
                    .add(Long.toString(getShaahZmanis16Point1Degrees))
                    .add(Long.toString(getShaahZmanis60Minutes))
                    .add(Long.toString(getShaahZmanis72Minutes))
                    .add(Long.toString(getShaahZmanis72MinutesZmanis))
                    .add(Long.toString(getShaahZmanis90Minutes))
                    .add(Long.toString(getShaahZmanis90MinutesZmanis))
                    .add(Long.toString(getShaahZmanis96MinutesZmanis))
                    .add(Long.toString(getShaahZmanisAteretTorah))
                    .add(Long.toString(getShaahZmanisAlos16Point1ToTzais3Point8))
                    .add(Long.toString(getShaahZmanisAlos16Point1ToTzais3Point7))
                    .add(Long.toString(getShaahZmanis96Minutes))
                    .add(Long.toString(getShaahZmanis120Minutes))
                    .add(Long.toString(getShaahZmanis120MinutesZmanis))
                    .add(getPlagHamincha120MinutesZmanis.toString())
                    .add(getPlagHamincha120Minutes.toString())
                    .add(getAlos60.toString())
                    .add(getAlos72Zmanis.toString())
                    .add(getAlos96.toString())
                    .add(getAlos90Zmanis.toString())
                    .add(getAlos96Zmanis.toString())
                    .add(getAlos90.toString())
                    .add(getAlos120.toString())
                    .add(getAlos120Zmanis.toString())
                    .add(getAlos26Degrees.toString())
                    .add(getAlos18Degrees.toString())
                    .add(getAlos19Degrees.toString())
                    .add(getAlos19Point8Degrees.toString())
                    .add(getAlos16Point1Degrees.toString())
                    .add(getMisheyakir11Point5Degrees.toString())
                    .add(getMisheyakir11Degrees.toString())
                    .add(getMisheyakir10Point2Degrees.toString())
                    .add(getMisheyakir7Point65Degrees.toString())
                    .add(getMisheyakir9Point5Degrees.toString())
                    .add(getSofZmanShmaMGA19Point8Degrees.toString())
                    .add(getSofZmanShmaMGA16Point1Degrees.toString())
                    .add(getSofZmanShmaMGA18Degrees.toString())
                    .add(getSofZmanShmaMGA72Minutes.toString())
                    .add(getSofZmanShmaMGA72MinutesZmanis.toString())
                    .add(getSofZmanShmaMGA90Minutes.toString())
                    .add(getSofZmanShmaMGA90MinutesZmanis.toString())
                    .add(getSofZmanShmaMGA96Minutes.toString())
                    .add(getSofZmanShmaMGA96MinutesZmanis.toString())
                    .add(getSofZmanShma3HoursBeforeChatzos.toString())
                    .add(getSofZmanShmaMGA120Minutes.toString())
                    .add(getSofZmanShmaAlos16Point1ToSunset.toString())
                    .add(getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees.toString())
                    .add(getSofZmanTfilaMGA19Point8Degrees.toString())
                    .add(getSofZmanTfilaMGA16Point1Degrees.toString())
                    .add(getSofZmanTfilaMGA18Degrees.toString())
                    .add(getSofZmanTfilaMGA72Minutes.toString())
                    .add(getSofZmanTfilaMGA72MinutesZmanis.toString())
                    .add(getSofZmanTfilaMGA90Minutes.toString())
                    .add(getSofZmanTfilaMGA90MinutesZmanis.toString())
                    .add(getSofZmanTfilaMGA96Minutes.toString())
                    .add(getSofZmanTfilaMGA96MinutesZmanis.toString())
                    .add(getSofZmanTfilaMGA120Minutes.toString())
                    .add(getSofZmanTfila2HoursBeforeChatzos.toString())
                    .add(getMinchaGedola30Minutes.toString())
                    .add(getMinchaGedola72Minutes.toString())
                    .add(getMinchaGedola16Point1Degrees.toString())
                    .add(getMinchaGedolaAhavatShalom.toString())
                    .add(getMinchaGedolaGreaterThan30.toString())
                    .add(getMinchaKetana16Point1Degrees.toString())
                    .add(getMinchaKetanaAhavatShalom.toString())
                    .add(getMinchaKetana72Minutes.toString())
                    .add(getPlagHamincha60Minutes.toString())
                    .add(getPlagHamincha72Minutes.toString())
                    .add(getPlagHamincha90Minutes.toString())
                    .add(getPlagHamincha96Minutes.toString())
                    .add(getPlagHamincha96MinutesZmanis.toString())
                    .add(getPlagHamincha90MinutesZmanis.toString())
                    .add(getPlagHamincha72MinutesZmanis.toString())
                    .add(getPlagHamincha16Point1Degrees.toString())
                    .add(getPlagHamincha19Point8Degrees.toString())
                    .add(getPlagHamincha26Degrees.toString())
                    .add(getPlagHamincha18Degrees.toString())
                    .add(getPlagAlosToSunset.toString())
                    .add(getPlagAlos16Point1ToTzaisGeonim7Point083Degrees.toString())
                    .add(getPlagAhavatShalom.toString())
                    .add(getBainHashmashosRT13Point24Degrees.toString())
                    .add(getBainHashmashosRT58Point5Minutes.toString())
                    .add(getBainHashmashosRT13Point5MinutesBefore7Point083Degrees.toString())
                    .add(getBainHashmashosRT2Stars.toString())
                    .add(getBainHashmashosYereim18Minutes.toString())
                    .add(getBainHashmashosYereim3Point05Degrees.toString())
                    .add(getBainHashmashosYereim16Point875Minutes.toString())
                    .add(getBainHashmashosYereim2Point8Degrees.toString())
                    .add(getBainHashmashosYereim13Point5Minutes.toString())
                    .add(getBainHashmashosYereim2Point1Degrees.toString())
                    .add(getTzaisGeonim3Point7Degrees.toString())
                    .add(getTzaisGeonim3Point8Degrees.toString())
                    .add(getTzaisGeonim5Point95Degrees.toString())
                    .add(getTzaisGeonim4Point61Degrees.toString())
                    .add(getTzaisGeonim4Point37Degrees.toString())
                    .add(getTzaisGeonim5Point88Degrees.toString())
                    .add(getTzaisGeonim4Point8Degrees.toString())
                    .add(getTzaisGeonim6Point45Degrees.toString())
                    .add(getTzaisGeonim7Point083Degrees.toString())
                    .add(getTzaisGeonim7Point67Degrees.toString())
                    .add(getTzaisGeonim8Point5Degrees.toString())
                    .add(getTzaisGeonim9Point3Degrees.toString())
                    .add(getTzaisGeonim9Point75Degrees.toString())
                    .add(getTzais60.toString())
                    .add(getTzaisAteretTorah.toString())
                    .add(getSofZmanShmaAteretTorah.toString())
                    .add(getSofZmanTfilahAteretTorah.toString())
                    .add(getMinchaGedolaAteretTorah.toString())
                    .add(getMinchaKetanaAteretTorah.toString())
                    .add(getPlagHaminchaAteretTorah.toString())
                    .add(getTzais72Zmanis.toString())
                    .add(getTzais90Zmanis.toString())
                    .add(getTzais96Zmanis.toString())
                    .add(getTzais90.toString())
                    .add(getTzais120.toString())
                    .add(getTzais120Zmanis.toString())
                    .add(getTzais16Point1Degrees.toString())
                    .add(getTzais26Degrees.toString())
                    .add(getTzais18Degrees.toString())
                    .add(getTzais19Point8Degrees.toString())
                    .add(getTzais96.toString())
                    .add(getFixedLocalChatzos.toString())
                    .add(getSofZmanKidushLevanaBetweenMoldos.toString())
                    .add(getSofZmanKidushLevana15Days.toString())
                    .add(getTchilasZmanKidushLevana3Days.toString())
                    .add(getZmanMolad.toString())
                    .add(getTchilasZmanKidushLevana7Days.toString())
                    .add(getSofZmanAchilasChametzGRA.toString())
                    .add(getSofZmanAchilasChametzMGA72Minutes.toString())
                    .add(getSofZmanAchilasChametzMGA16Point1Degrees.toString())
                    .add(getSofZmanBiurChametzGRA.toString())
                    .add(getSofZmanBiurChametzMGA72Minutes.toString())
                    .add(getSofZmanBiurChametzMGA16Point1Degrees.toString())
                    .add(getSolarMidnight.toString())
                    .add(Long.toString(getShaahZmanisBaalHatanya))
                    .add(getAlosBaalHatanya.toString())
                    .add(getSofZmanShmaBaalHatanya.toString())
                    .add(getSofZmanTfilaBaalHatanya.toString())
                    .add(getSofZmanAchilasChametzBaalHatanya.toString())
                    .add(getSofZmanBiurChametzBaalHatanya.toString())
                    .add(getMinchaGedolaBaalHatanya.toString())
                    .add(getMinchaGedolaBaalHatanyaGreaterThan30.toString())
                    .add(getMinchaKetanaBaalHatanya.toString())
                    .add(getPlagHaminchaBaalHatanya.toString())
                    .add(getTzaisBaalHatanya.toString())
                    .add(getSofZmanShmaMGA18DegreesToFixedLocalChatzos.toString())
                    .add(getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos.toString())
                    .add(getSofZmanShmaMGA90MinutesToFixedLocalChatzos.toString())
                    .add(getSofZmanShmaMGA72MinutesToFixedLocalChatzos.toString())
                    .add(getSofZmanShmaGRASunriseToFixedLocalChatzos.toString())
                    .add(getSofZmanTfilaGRASunriseToFixedLocalChatzos.toString())
                    .add(getMinchaGedolaGRAFixedLocalChatzos30Minutes.toString())
                    .add(getMinchaKetanaGRAFixedLocalChatzosToSunset.toString())
                    .add(getPlagHaminchaGRAFixedLocalChatzosToSunset.toString())
                    .add(getTzais50.toString())
                    .add(getSamuchLeMinchaKetanaGRA.toString())
                    .add(getSamuchLeMinchaKetana16Point1Degrees.toString())
                    .add(getSamuchLeMinchaKetana72Minutes.toString())
                    .toString();
        }

		public FullZmanim(long getShaahZmanis19Point8Degrees, long getShaahZmanis18Degrees,
				long getShaahZmanis26Degrees, long getShaahZmanis16Point1Degrees, long getShaahZmanis60Minutes,
				long getShaahZmanis72Minutes, long getShaahZmanis72MinutesZmanis, long getShaahZmanis90Minutes,
				long getShaahZmanis90MinutesZmanis, long getShaahZmanis96MinutesZmanis, long getShaahZmanisAteretTorah,
				long getShaahZmanisAlos16Point1ToTzais3Point8, long getShaahZmanisAlos16Point1ToTzais3Point7,
				long getShaahZmanis96Minutes, long getShaahZmanis120Minutes, long getShaahZmanis120MinutesZmanis,
				Date getPlagHamincha120MinutesZmanis, Date getPlagHamincha120Minutes, Date getAlos60,
				Date getAlos72Zmanis, Date getAlos96, Date getAlos90Zmanis, Date getAlos96Zmanis, Date getAlos90,
				Date getAlos120, Date getAlos120Zmanis, Date getAlos26Degrees, Date getAlos18Degrees,
				Date getAlos19Degrees, Date getAlos19Point8Degrees, Date getAlos16Point1Degrees,
				Date getMisheyakir11Point5Degrees, Date getMisheyakir11Degrees, Date getMisheyakir10Point2Degrees,
				Date getMisheyakir7Point65Degrees, Date getMisheyakir9Point5Degrees,
				Date getSofZmanShmaMGA19Point8Degrees, Date getSofZmanShmaMGA16Point1Degrees,
				Date getSofZmanShmaMGA18Degrees, Date getSofZmanShmaMGA72Minutes, Date getSofZmanShmaMGA72MinutesZmanis,
				Date getSofZmanShmaMGA90Minutes, Date getSofZmanShmaMGA90MinutesZmanis, Date getSofZmanShmaMGA96Minutes,
				Date getSofZmanShmaMGA96MinutesZmanis, Date getSofZmanShma3HoursBeforeChatzos,
				Date getSofZmanShmaMGA120Minutes, Date getSofZmanShmaAlos16Point1ToSunset,
				Date getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees,
				Date getSofZmanTfilaMGA19Point8Degrees, Date getSofZmanTfilaMGA16Point1Degrees,
				Date getSofZmanTfilaMGA18Degrees, Date getSofZmanTfilaMGA72Minutes,
				Date getSofZmanTfilaMGA72MinutesZmanis, Date getSofZmanTfilaMGA90Minutes,
				Date getSofZmanTfilaMGA90MinutesZmanis, Date getSofZmanTfilaMGA96Minutes,
				Date getSofZmanTfilaMGA96MinutesZmanis, Date getSofZmanTfilaMGA120Minutes,
				Date getSofZmanTfila2HoursBeforeChatzos, Date getMinchaGedola30Minutes, Date getMinchaGedola72Minutes,
				Date getMinchaGedola16Point1Degrees, Date getMinchaGedolaAhavatShalom,
				Date getMinchaGedolaGreaterThan30, Date getMinchaKetana16Point1Degrees,
				Date getMinchaKetanaAhavatShalom, Date getMinchaKetana72Minutes, Date getPlagHamincha60Minutes,
				Date getPlagHamincha72Minutes, Date getPlagHamincha90Minutes, Date getPlagHamincha96Minutes,
				Date getPlagHamincha96MinutesZmanis, Date getPlagHamincha90MinutesZmanis,
				Date getPlagHamincha72MinutesZmanis, Date getPlagHamincha16Point1Degrees,
				Date getPlagHamincha19Point8Degrees, Date getPlagHamincha26Degrees, Date getPlagHamincha18Degrees,
				Date getPlagAlosToSunset, Date getPlagAlos16Point1ToTzaisGeonim7Point083Degrees,
				Date getPlagAhavatShalom, Date getBainHashmashosRT13Point24Degrees,
				Date getBainHashmashosRT58Point5Minutes, Date getBainHashmashosRT13Point5MinutesBefore7Point083Degrees,
				Date getBainHashmashosRT2Stars, Date getBainHashmashosYereim18Minutes,
				Date getBainHashmashosYereim3Point05Degrees, Date getBainHashmashosYereim16Point875Minutes,
				Date getBainHashmashosYereim2Point8Degrees, Date getBainHashmashosYereim13Point5Minutes,
				Date getBainHashmashosYereim2Point1Degrees, Date getTzaisGeonim3Point7Degrees,
				Date getTzaisGeonim3Point8Degrees, Date getTzaisGeonim5Point95Degrees,
				Date getTzaisGeonim4Point61Degrees, Date getTzaisGeonim4Point37Degrees,
				Date getTzaisGeonim5Point88Degrees, Date getTzaisGeonim4Point8Degrees,
				Date getTzaisGeonim6Point45Degrees, Date getTzaisGeonim7Point083Degrees,
				Date getTzaisGeonim7Point67Degrees, Date getTzaisGeonim8Point5Degrees,
				Date getTzaisGeonim9Point3Degrees, Date getTzaisGeonim9Point75Degrees, Date getTzais60,
				Date getTzaisAteretTorah, Date getSofZmanShmaAteretTorah, Date getSofZmanTfilahAteretTorah,
				Date getMinchaGedolaAteretTorah, Date getMinchaKetanaAteretTorah, Date getPlagHaminchaAteretTorah,
				Date getTzais72Zmanis, Date getTzais90Zmanis, Date getTzais96Zmanis, Date getTzais90, Date getTzais120,
				Date getTzais120Zmanis, Date getTzais16Point1Degrees, Date getTzais26Degrees, Date getTzais18Degrees,
				Date getTzais19Point8Degrees, Date getTzais96, Date getFixedLocalChatzos,
				Date getSofZmanKidushLevanaBetweenMoldos,
				Date getSofZmanKidushLevana15Days, Date getTchilasZmanKidushLevana3Days, Date getZmanMolad,
				Date getTchilasZmanKidushLevana7Days, Date getSofZmanAchilasChametzGRA,
				Date getSofZmanAchilasChametzMGA72Minutes, Date getSofZmanAchilasChametzMGA16Point1Degrees,
				Date getSofZmanBiurChametzGRA, Date getSofZmanBiurChametzMGA72Minutes,
				Date getSofZmanBiurChametzMGA16Point1Degrees, Date getSolarMidnight, long getShaahZmanisBaalHatanya,
				Date getAlosBaalHatanya, Date getSofZmanShmaBaalHatanya, Date getSofZmanTfilaBaalHatanya,
				Date getSofZmanAchilasChametzBaalHatanya, Date getSofZmanBiurChametzBaalHatanya,
				Date getMinchaGedolaBaalHatanya, Date getMinchaGedolaBaalHatanyaGreaterThan30,
				Date getMinchaKetanaBaalHatanya, Date getPlagHaminchaBaalHatanya, Date getTzaisBaalHatanya,
				Date getSofZmanShmaMGA18DegreesToFixedLocalChatzos,
				Date getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos,
				Date getSofZmanShmaMGA90MinutesToFixedLocalChatzos, Date getSofZmanShmaMGA72MinutesToFixedLocalChatzos,
				Date getSofZmanShmaGRASunriseToFixedLocalChatzos, Date getSofZmanTfilaGRASunriseToFixedLocalChatzos,
				Date getMinchaGedolaGRAFixedLocalChatzos30Minutes, Date getMinchaKetanaGRAFixedLocalChatzosToSunset,
				Date getPlagHaminchaGRAFixedLocalChatzosToSunset, Date getTzais50, Date getSamuchLeMinchaKetanaGRA,
				Date getSamuchLeMinchaKetana16Point1Degrees, Date getSamuchLeMinchaKetana72Minutes) {
            this.getShaahZmanis19Point8Degrees = getShaahZmanis19Point8Degrees;
            this.getShaahZmanis18Degrees = getShaahZmanis18Degrees;
            this.getShaahZmanis26Degrees = getShaahZmanis26Degrees;
            this.getShaahZmanis16Point1Degrees = getShaahZmanis16Point1Degrees;
            this.getShaahZmanis60Minutes = getShaahZmanis60Minutes;
            this.getShaahZmanis72Minutes = getShaahZmanis72Minutes;
            this.getShaahZmanis72MinutesZmanis = getShaahZmanis72MinutesZmanis;
            this.getShaahZmanis90Minutes = getShaahZmanis90Minutes;
            this.getShaahZmanis90MinutesZmanis = getShaahZmanis90MinutesZmanis;
            this.getShaahZmanis96MinutesZmanis = getShaahZmanis96MinutesZmanis;
            this.getShaahZmanisAteretTorah = getShaahZmanisAteretTorah;
            this.getShaahZmanisAlos16Point1ToTzais3Point8 = getShaahZmanisAlos16Point1ToTzais3Point8;
            this.getShaahZmanisAlos16Point1ToTzais3Point7 = getShaahZmanisAlos16Point1ToTzais3Point7;
            this.getShaahZmanis96Minutes = getShaahZmanis96Minutes;
            this.getShaahZmanis120Minutes = getShaahZmanis120Minutes;
            this.getShaahZmanis120MinutesZmanis = getShaahZmanis120MinutesZmanis;
            this.getPlagHamincha120MinutesZmanis = getPlagHamincha120MinutesZmanis;
            this.getPlagHamincha120Minutes = getPlagHamincha120Minutes;
            this.getAlos60 = getAlos60;
            this.getAlos72Zmanis = getAlos72Zmanis;
            this.getAlos96 = getAlos96;
            this.getAlos90Zmanis = getAlos90Zmanis;
            this.getAlos96Zmanis = getAlos96Zmanis;
            this.getAlos90 = getAlos90;
            this.getAlos120 = getAlos120;
            this.getAlos120Zmanis = getAlos120Zmanis;
            this.getAlos26Degrees = getAlos26Degrees;
            this.getAlos18Degrees = getAlos18Degrees;
            this.getAlos19Degrees = getAlos19Degrees;
            this.getAlos19Point8Degrees = getAlos19Point8Degrees;
            this.getAlos16Point1Degrees = getAlos16Point1Degrees;
            this.getMisheyakir11Point5Degrees = getMisheyakir11Point5Degrees;
            this.getMisheyakir11Degrees = getMisheyakir11Degrees;
            this.getMisheyakir10Point2Degrees = getMisheyakir10Point2Degrees;
            this.getMisheyakir7Point65Degrees = getMisheyakir7Point65Degrees;
            this.getMisheyakir9Point5Degrees = getMisheyakir9Point5Degrees;
            this.getSofZmanShmaMGA19Point8Degrees = getSofZmanShmaMGA19Point8Degrees;
            this.getSofZmanShmaMGA16Point1Degrees = getSofZmanShmaMGA16Point1Degrees;
            this.getSofZmanShmaMGA18Degrees = getSofZmanShmaMGA18Degrees;
            this.getSofZmanShmaMGA72Minutes = getSofZmanShmaMGA72Minutes;
            this.getSofZmanShmaMGA72MinutesZmanis = getSofZmanShmaMGA72MinutesZmanis;
            this.getSofZmanShmaMGA90Minutes = getSofZmanShmaMGA90Minutes;
            this.getSofZmanShmaMGA90MinutesZmanis = getSofZmanShmaMGA90MinutesZmanis;
            this.getSofZmanShmaMGA96Minutes = getSofZmanShmaMGA96Minutes;
            this.getSofZmanShmaMGA96MinutesZmanis = getSofZmanShmaMGA96MinutesZmanis;
            this.getSofZmanShma3HoursBeforeChatzos = getSofZmanShma3HoursBeforeChatzos;
            this.getSofZmanShmaMGA120Minutes = getSofZmanShmaMGA120Minutes;
            this.getSofZmanShmaAlos16Point1ToSunset = getSofZmanShmaAlos16Point1ToSunset;
            this.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees = getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees;
            this.getSofZmanTfilaMGA19Point8Degrees = getSofZmanTfilaMGA19Point8Degrees;
            this.getSofZmanTfilaMGA16Point1Degrees = getSofZmanTfilaMGA16Point1Degrees;
            this.getSofZmanTfilaMGA18Degrees = getSofZmanTfilaMGA18Degrees;
            this.getSofZmanTfilaMGA72Minutes = getSofZmanTfilaMGA72Minutes;
            this.getSofZmanTfilaMGA72MinutesZmanis = getSofZmanTfilaMGA72MinutesZmanis;
            this.getSofZmanTfilaMGA90Minutes = getSofZmanTfilaMGA90Minutes;
            this.getSofZmanTfilaMGA90MinutesZmanis = getSofZmanTfilaMGA90MinutesZmanis;
            this.getSofZmanTfilaMGA96Minutes = getSofZmanTfilaMGA96Minutes;
            this.getSofZmanTfilaMGA96MinutesZmanis = getSofZmanTfilaMGA96MinutesZmanis;
            this.getSofZmanTfilaMGA120Minutes = getSofZmanTfilaMGA120Minutes;
            this.getSofZmanTfila2HoursBeforeChatzos = getSofZmanTfila2HoursBeforeChatzos;
            this.getMinchaGedola30Minutes = getMinchaGedola30Minutes;
            this.getMinchaGedola72Minutes = getMinchaGedola72Minutes;
            this.getMinchaGedola16Point1Degrees = getMinchaGedola16Point1Degrees;
            this.getMinchaGedolaAhavatShalom = getMinchaGedolaAhavatShalom;
            this.getMinchaGedolaGreaterThan30 = getMinchaGedolaGreaterThan30;
            this.getMinchaKetana16Point1Degrees = getMinchaKetana16Point1Degrees;
            this.getMinchaKetanaAhavatShalom = getMinchaKetanaAhavatShalom;
            this.getMinchaKetana72Minutes = getMinchaKetana72Minutes;
            this.getPlagHamincha60Minutes = getPlagHamincha60Minutes;
            this.getPlagHamincha72Minutes = getPlagHamincha72Minutes;
            this.getPlagHamincha90Minutes = getPlagHamincha90Minutes;
            this.getPlagHamincha96Minutes = getPlagHamincha96Minutes;
            this.getPlagHamincha96MinutesZmanis = getPlagHamincha96MinutesZmanis;
            this.getPlagHamincha90MinutesZmanis = getPlagHamincha90MinutesZmanis;
            this.getPlagHamincha72MinutesZmanis = getPlagHamincha72MinutesZmanis;
            this.getPlagHamincha16Point1Degrees = getPlagHamincha16Point1Degrees;
            this.getPlagHamincha19Point8Degrees = getPlagHamincha19Point8Degrees;
            this.getPlagHamincha26Degrees = getPlagHamincha26Degrees;
            this.getPlagHamincha18Degrees = getPlagHamincha18Degrees;
            this.getPlagAlosToSunset = getPlagAlosToSunset;
            this.getPlagAlos16Point1ToTzaisGeonim7Point083Degrees = getPlagAlos16Point1ToTzaisGeonim7Point083Degrees;
            this.getPlagAhavatShalom = getPlagAhavatShalom;
            this.getBainHashmashosRT13Point24Degrees = getBainHashmashosRT13Point24Degrees;
            this.getBainHashmashosRT58Point5Minutes = getBainHashmashosRT58Point5Minutes;
            this.getBainHashmashosRT13Point5MinutesBefore7Point083Degrees = getBainHashmashosRT13Point5MinutesBefore7Point083Degrees;
            this.getBainHashmashosRT2Stars = getBainHashmashosRT2Stars;
            this.getBainHashmashosYereim18Minutes = getBainHashmashosYereim18Minutes;
            this.getBainHashmashosYereim3Point05Degrees = getBainHashmashosYereim3Point05Degrees;
            this.getBainHashmashosYereim16Point875Minutes = getBainHashmashosYereim16Point875Minutes;
            this.getBainHashmashosYereim2Point8Degrees = getBainHashmashosYereim2Point8Degrees;
            this.getBainHashmashosYereim13Point5Minutes = getBainHashmashosYereim13Point5Minutes;
            this.getBainHashmashosYereim2Point1Degrees = getBainHashmashosYereim2Point1Degrees;
            this.getTzaisGeonim3Point7Degrees = getTzaisGeonim3Point7Degrees;
            this.getTzaisGeonim3Point8Degrees = getTzaisGeonim3Point8Degrees;
            this.getTzaisGeonim5Point95Degrees = getTzaisGeonim5Point95Degrees;
            this.getTzaisGeonim4Point61Degrees = getTzaisGeonim4Point61Degrees;
            this.getTzaisGeonim4Point37Degrees = getTzaisGeonim4Point37Degrees;
            this.getTzaisGeonim5Point88Degrees = getTzaisGeonim5Point88Degrees;
            this.getTzaisGeonim4Point8Degrees = getTzaisGeonim4Point8Degrees;
            this.getTzaisGeonim6Point45Degrees = getTzaisGeonim6Point45Degrees;
            this.getTzaisGeonim7Point083Degrees = getTzaisGeonim7Point083Degrees;
            this.getTzaisGeonim7Point67Degrees = getTzaisGeonim7Point67Degrees;
            this.getTzaisGeonim8Point5Degrees = getTzaisGeonim8Point5Degrees;
            this.getTzaisGeonim9Point3Degrees = getTzaisGeonim9Point3Degrees;
            this.getTzaisGeonim9Point75Degrees = getTzaisGeonim9Point75Degrees;
            this.getTzais60 = getTzais60;
            this.getTzaisAteretTorah = getTzaisAteretTorah;
            this.getSofZmanShmaAteretTorah = getSofZmanShmaAteretTorah;
            this.getSofZmanTfilahAteretTorah = getSofZmanTfilahAteretTorah;
            this.getMinchaGedolaAteretTorah = getMinchaGedolaAteretTorah;
            this.getMinchaKetanaAteretTorah = getMinchaKetanaAteretTorah;
            this.getPlagHaminchaAteretTorah = getPlagHaminchaAteretTorah;
            this.getTzais72Zmanis = getTzais72Zmanis;
            this.getTzais90Zmanis = getTzais90Zmanis;
            this.getTzais96Zmanis = getTzais96Zmanis;
            this.getTzais90 = getTzais90;
            this.getTzais120 = getTzais120;
            this.getTzais120Zmanis = getTzais120Zmanis;
            this.getTzais16Point1Degrees = getTzais16Point1Degrees;
            this.getTzais26Degrees = getTzais26Degrees;
            this.getTzais18Degrees = getTzais18Degrees;
            this.getTzais19Point8Degrees = getTzais19Point8Degrees;
            this.getTzais96 = getTzais96;
            this.getFixedLocalChatzos = getFixedLocalChatzos;
            this.getSofZmanShmaFixedLocal = getSofZmanShmaFixedLocal;
            this.getSofZmanTfilaFixedLocal = getSofZmanTfilaFixedLocal;
            this.getSofZmanKidushLevanaBetweenMoldos = getSofZmanKidushLevanaBetweenMoldos;
            this.getSofZmanKidushLevana15Days = getSofZmanKidushLevana15Days;
            this.getTchilasZmanKidushLevana3Days = getTchilasZmanKidushLevana3Days;
            this.getZmanMolad = getZmanMolad;
            this.getTchilasZmanKidushLevana7Days = getTchilasZmanKidushLevana7Days;
            this.getSofZmanAchilasChametzGRA = getSofZmanAchilasChametzGRA;
            this.getSofZmanAchilasChametzMGA72Minutes = getSofZmanAchilasChametzMGA72Minutes;
            this.getSofZmanAchilasChametzMGA16Point1Degrees = getSofZmanAchilasChametzMGA16Point1Degrees;
            this.getSofZmanBiurChametzGRA = getSofZmanBiurChametzGRA;
            this.getSofZmanBiurChametzMGA72Minutes = getSofZmanBiurChametzMGA72Minutes;
            this.getSofZmanBiurChametzMGA16Point1Degrees = getSofZmanBiurChametzMGA16Point1Degrees;
            this.getSolarMidnight = getSolarMidnight;
            this.getShaahZmanisBaalHatanya = getShaahZmanisBaalHatanya;
            this.getAlosBaalHatanya = getAlosBaalHatanya;
            this.getSofZmanShmaBaalHatanya = getSofZmanShmaBaalHatanya;
            this.getSofZmanTfilaBaalHatanya = getSofZmanTfilaBaalHatanya;
            this.getSofZmanAchilasChametzBaalHatanya = getSofZmanAchilasChametzBaalHatanya;
            this.getSofZmanBiurChametzBaalHatanya = getSofZmanBiurChametzBaalHatanya;
            this.getMinchaGedolaBaalHatanya = getMinchaGedolaBaalHatanya;
            this.getMinchaGedolaBaalHatanyaGreaterThan30 = getMinchaGedolaBaalHatanyaGreaterThan30;
            this.getMinchaKetanaBaalHatanya = getMinchaKetanaBaalHatanya;
            this.getPlagHaminchaBaalHatanya = getPlagHaminchaBaalHatanya;
            this.getTzaisBaalHatanya = getTzaisBaalHatanya;
            this.getSofZmanShmaMGA18DegreesToFixedLocalChatzos = getSofZmanShmaMGA18DegreesToFixedLocalChatzos;
            this.getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos = getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos;
            this.getSofZmanShmaMGA90MinutesToFixedLocalChatzos = getSofZmanShmaMGA90MinutesToFixedLocalChatzos;
            this.getSofZmanShmaMGA72MinutesToFixedLocalChatzos = getSofZmanShmaMGA72MinutesToFixedLocalChatzos;
            this.getSofZmanShmaGRASunriseToFixedLocalChatzos = getSofZmanShmaGRASunriseToFixedLocalChatzos;
            this.getSofZmanTfilaGRASunriseToFixedLocalChatzos = getSofZmanTfilaGRASunriseToFixedLocalChatzos;
            this.getMinchaGedolaGRAFixedLocalChatzos30Minutes = getMinchaGedolaGRAFixedLocalChatzos30Minutes;
            this.getMinchaKetanaGRAFixedLocalChatzosToSunset = getMinchaKetanaGRAFixedLocalChatzosToSunset;
            this.getPlagHaminchaGRAFixedLocalChatzosToSunset = getPlagHaminchaGRAFixedLocalChatzosToSunset;
            this.getTzais50 = getTzais50;
            this.getSamuchLeMinchaKetanaGRA = getSamuchLeMinchaKetanaGRA;
            this.getSamuchLeMinchaKetana16Point1Degrees = getSamuchLeMinchaKetana16Point1Degrees;
            this.getSamuchLeMinchaKetana72Minutes = getSamuchLeMinchaKetana72Minutes;
        }

        public final long getShaahZmanis19Point8Degrees;
        public final long getShaahZmanis18Degrees;
        public final long getShaahZmanis26Degrees;
        public final long getShaahZmanis16Point1Degrees;
        public final long getShaahZmanis60Minutes;
        public final long getShaahZmanis72Minutes;
        public final long getShaahZmanis72MinutesZmanis;
        public final long getShaahZmanis90Minutes;
        public final long getShaahZmanis90MinutesZmanis;
        public final long getShaahZmanis96MinutesZmanis;
        public final long getShaahZmanisAteretTorah;
        public final long getShaahZmanisAlos16Point1ToTzais3Point8;
        public final long getShaahZmanisAlos16Point1ToTzais3Point7;
        public final long getShaahZmanis96Minutes;
        public final long getShaahZmanis120Minutes;
        public final long getShaahZmanis120MinutesZmanis;
        public final Date getPlagHamincha120MinutesZmanis;
        public final Date getPlagHamincha120Minutes;
        public final Date getAlos60;
        public final Date getAlos72Zmanis;
        public final Date getAlos96;
        public final Date getAlos90Zmanis;
        public final Date getAlos96Zmanis;
        public final Date getAlos90;
        public final Date getAlos120;
        public final Date getAlos120Zmanis;
        public final Date getAlos26Degrees;
        public final Date getAlos18Degrees;
        public final Date getAlos19Degrees;
        public final Date getAlos19Point8Degrees;
        public final Date getAlos16Point1Degrees;
        public final Date getMisheyakir11Point5Degrees;
        public final Date getMisheyakir11Degrees;
        public final Date getMisheyakir10Point2Degrees;
        public final Date getMisheyakir7Point65Degrees;
        public final Date getMisheyakir9Point5Degrees;
        public final Date getSofZmanShmaMGA19Point8Degrees;
        public final Date getSofZmanShmaMGA16Point1Degrees;
        public final Date getSofZmanShmaMGA18Degrees;
        public final Date getSofZmanShmaMGA72Minutes;
        public final Date getSofZmanShmaMGA72MinutesZmanis;
        public final Date getSofZmanShmaMGA90Minutes;
        public final Date getSofZmanShmaMGA90MinutesZmanis;
        public final Date getSofZmanShmaMGA96Minutes;
        public final Date getSofZmanShmaMGA96MinutesZmanis;
        public final Date getSofZmanShma3HoursBeforeChatzos;
        public final Date getSofZmanShmaMGA120Minutes;
        public final Date getSofZmanShmaAlos16Point1ToSunset;
        public final Date getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees;
        public final Date getSofZmanTfilaMGA19Point8Degrees;
        public final Date getSofZmanTfilaMGA16Point1Degrees;
        public final Date getSofZmanTfilaMGA18Degrees;
        public final Date getSofZmanTfilaMGA72Minutes;
        public final Date getSofZmanTfilaMGA72MinutesZmanis;
        public final Date getSofZmanTfilaMGA90Minutes;
        public final Date getSofZmanTfilaMGA90MinutesZmanis;
        public final Date getSofZmanTfilaMGA96Minutes;
        public final Date getSofZmanTfilaMGA96MinutesZmanis;
        public final Date getSofZmanTfilaMGA120Minutes;
        public final Date getSofZmanTfila2HoursBeforeChatzos;
        public final Date getMinchaGedola30Minutes;
        public final Date getMinchaGedola72Minutes;
        public final Date getMinchaGedola16Point1Degrees;
        public final Date getMinchaGedolaAhavatShalom;
        public final Date getMinchaGedolaGreaterThan30;
        public final Date getMinchaKetana16Point1Degrees;
        public final Date getMinchaKetanaAhavatShalom;
        public final Date getMinchaKetana72Minutes;
        public final Date getPlagHamincha60Minutes;
        public final Date getPlagHamincha72Minutes;
        public final Date getPlagHamincha90Minutes;
        public final Date getPlagHamincha96Minutes;
        public final Date getPlagHamincha96MinutesZmanis;
        public final Date getPlagHamincha90MinutesZmanis;
        public final Date getPlagHamincha72MinutesZmanis;
        public final Date getPlagHamincha16Point1Degrees;
        public final Date getPlagHamincha19Point8Degrees;
        public final Date getPlagHamincha26Degrees;
        public final Date getPlagHamincha18Degrees;
        public final Date getPlagAlosToSunset;
        public final Date getPlagAlos16Point1ToTzaisGeonim7Point083Degrees;
        public final Date getPlagAhavatShalom;
        public final Date getBainHashmashosRT13Point24Degrees;
        public final Date getBainHashmashosRT58Point5Minutes;
        public final Date getBainHashmashosRT13Point5MinutesBefore7Point083Degrees;
        public final Date getBainHashmashosRT2Stars;
        public final Date getBainHashmashosYereim18Minutes;
        public final Date getBainHashmashosYereim3Point05Degrees;
        public final Date getBainHashmashosYereim16Point875Minutes;
        public final Date getBainHashmashosYereim2Point8Degrees;
        public final Date getBainHashmashosYereim13Point5Minutes;
        public final Date getBainHashmashosYereim2Point1Degrees;
        public final Date getTzaisGeonim3Point7Degrees;
        public final Date getTzaisGeonim3Point8Degrees;
        public final Date getTzaisGeonim5Point95Degrees;
        public final Date getTzaisGeonim4Point61Degrees;
        public final Date getTzaisGeonim4Point37Degrees;
        public final Date getTzaisGeonim5Point88Degrees;
        public final Date getTzaisGeonim4Point8Degrees;
        public final Date getTzaisGeonim6Point45Degrees;
        public final Date getTzaisGeonim7Point083Degrees;
        public final Date getTzaisGeonim7Point67Degrees;
        public final Date getTzaisGeonim8Point5Degrees;
        public final Date getTzaisGeonim9Point3Degrees;
        public final Date getTzaisGeonim9Point75Degrees;
        public final Date getTzais60;
        public final Date getTzaisAteretTorah;
        public final Date getSofZmanShmaAteretTorah;
        public final Date getSofZmanTfilahAteretTorah;
        public final Date getMinchaGedolaAteretTorah;
        public final Date getMinchaKetanaAteretTorah;
        public final Date getPlagHaminchaAteretTorah;
        public final Date getTzais72Zmanis;
        public final Date getTzais90Zmanis;
        public final Date getTzais96Zmanis;
        public final Date getTzais90;
        public final Date getTzais120;
        public final Date getTzais120Zmanis;
        public final Date getTzais16Point1Degrees;
        public final Date getTzais26Degrees;
        public final Date getTzais18Degrees;
        public final Date getTzais19Point8Degrees;
        public final Date getTzais96;
        public final Date getFixedLocalChatzos;
        public final Date getSofZmanShmaFixedLocal;
        public final Date getSofZmanTfilaFixedLocal;
        public final Date getSofZmanKidushLevanaBetweenMoldos;
        public final Date getSofZmanKidushLevana15Days;
        public final Date getTchilasZmanKidushLevana3Days;
        public final Date getZmanMolad;
        public final Date getTchilasZmanKidushLevana7Days;
        public final Date getSofZmanAchilasChametzGRA;
        public final Date getSofZmanAchilasChametzMGA72Minutes;
        public final Date getSofZmanAchilasChametzMGA16Point1Degrees;
        public final Date getSofZmanBiurChametzGRA;
        public final Date getSofZmanBiurChametzMGA72Minutes;
        public final Date getSofZmanBiurChametzMGA16Point1Degrees;
        public final Date getSolarMidnight;
        public final long getShaahZmanisBaalHatanya;
        public final Date getAlosBaalHatanya;
        public final Date getSofZmanShmaBaalHatanya;
        public final Date getSofZmanTfilaBaalHatanya;
        public final Date getSofZmanAchilasChametzBaalHatanya;
        public final Date getSofZmanBiurChametzBaalHatanya;
        public final Date getMinchaGedolaBaalHatanya;
        public final Date getMinchaGedolaBaalHatanyaGreaterThan30;
        public final Date getMinchaKetanaBaalHatanya;
        public final Date getPlagHaminchaBaalHatanya;
        public final Date getTzaisBaalHatanya;
        public final Date getSofZmanShmaMGA18DegreesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA90MinutesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA72MinutesToFixedLocalChatzos;
        public final Date getSofZmanShmaGRASunriseToFixedLocalChatzos;
        public final Date getSofZmanTfilaGRASunriseToFixedLocalChatzos;
        public final Date getMinchaGedolaGRAFixedLocalChatzos30Minutes;
        public final Date getMinchaKetanaGRAFixedLocalChatzosToSunset;
        public final Date getPlagHaminchaGRAFixedLocalChatzosToSunset;
        public final Date getTzais50;
        public final Date getSamuchLeMinchaKetanaGRA;
        public final Date getSamuchLeMinchaKetana16Point1Degrees;
        public final Date getSamuchLeMinchaKetana72Minutes;
    }
    
    /*static class FullAstronomicalCalculator {
        Date getSunrise
        Date getSeaLevelSunrise
        Date getBeginCivilTwilight
        Date getBeginNauticalTwilight
        Date getBeginAstronomicalTwilight
        Date getSunset
        Date getSeaLevelSunset
        Date getEndCivilTwilight
        Date getEndNauticalTwilight
        Date getEndAstronomicalTwilight
        Date getTimeOffset
        Date getTimeOffset
        Date getSunriseOffsetByDegrees
        Date getSunsetOffsetByDegrees
        double getUTCSunrise
        double getUTCSeaLevelSunrise
        double getUTCSunset
        double getUTCSeaLevelSunset
        long getTemporalHour
        long getTemporalHour
        Date getSunTransit
        Date getSunTransit
        Date getDateFromTime
        double getSunriseSolarDipFromOffset
        double getSunsetSolarDipFromOffset
        Calendar getAdjustedCalendar
        GeoLocation getGeoLocation
        AstronomicalCalculator getAstronomicalCalculator
        Calendar getCalendar
    }*/

    static class FullCalendar {
        public static final String fields = "current,currentJewishDate,yomTovIndex,dafYomiBavli,dafYomiYerushalmi,isruChag,birkasHachamah,parshah,upcomingParshah,specialShabbos,yomTov,yomTovAssurBemelacha,assurBemelacha,hasCandleLighting,tomorrowShabbosOrYomTov,erevYomTovSheni,aseresYemeiTeshuva,pesach,cholHamoedPesach,shavuos,roshHashana,yomKippur,succos,hoshanaRabba,shminiAtzeres,simchasTorah,cholHamoedSuccos,cholHamoed,erevYomTov,erevRoshChodesh,yomKippurKatan,beHaB,taanis,taanisBechoros,dayOfChanukah,chanukah,purim,roshChodesh,macharChodesh,shabbosMevorchim,dayOfOmer,tishaBav,molad,moladAsDate,tchilasZmanKidushLevana3Days,tchilasZmanKidushLevana7Days,sofZmanKidushLevanaBetweenMoldos,sofZmanKidushLevana15Days,tekufasTishreiElapsedDays";

        @Override
        public String toString() {
            //escaped for jewish date which contains comma, all escaped for ease of parsing
            return new StringJoiner(",")
                    .add("\"" + current + "\"")
                    .add("\"" + currentJewishDate + "\"")
                    .add("\"" + yomTovIndex + "\"")
                    .add("\"" + dafYomiBavli + "\"")
                    .add("\"" + dafYomiYerushalmi + "\"")
                    .add("\"" + isruChag + "\"")
                    .add("\"" + birkasHachamah + "\"")
                    .add("\"" + parshah + "\"")
                    .add("\"" + upcomingParshah + "\"")
                    .add("\"" + specialShabbos + "\"")
                    .add("\"" + yomTov + "\"")
                    .add("\"" + yomTovAssurBemelacha + "\"")
                    .add("\"" + assurBemelacha + "\"")
                    .add("\"" + hasCandleLighting + "\"")
                    .add("\"" + tomorrowShabbosOrYomTov + "\"")
                    .add("\"" + erevYomTovSheni + "\"")
                    .add("\"" + aseresYemeiTeshuva + "\"")
                    .add("\"" + pesach + "\"")
                    .add("\"" + cholHamoedPesach + "\"")
                    .add("\"" + shavuos + "\"")
                    .add("\"" + roshHashana + "\"")
                    .add("\"" + yomKippur + "\"")
                    .add("\"" + succos + "\"")
                    .add("\"" + hoshanaRabba + "\"")
                    .add("\"" + shminiAtzeres + "\"")
                    .add("\"" + simchasTorah + "\"")
                    .add("\"" + cholHamoedSuccos + "\"")
                    .add("\"" + cholHamoed + "\"")
                    .add("\"" + erevYomTov + "\"")
                    .add("\"" + erevRoshChodesh + "\"")
                    .add("\"" + yomKippurKatan + "\"")
                    .add("\"" + beHaB + "\"")
                    .add("\"" + taanis + "\"")
                    .add("\"" + taanisBechoros + "\"")
                    .add("\"" + dayOfChanukah + "\"")
                    .add("\"" + chanukah + "\"")
                    .add("\"" + purim + "\"")
                    .add("\"" + roshChodesh + "\"")
                    .add("\"" + macharChodesh + "\"")
                    .add("\"" + shabbosMevorchim + "\"")
                    .add("\"" + dayOfOmer + "\"")
                    .add("\"" + tishaBav + "\"")
                    .add("\"" + molad + "\"")
                    .add("\"" + moladAsDate + "\"")
                    .add("\"" + tchilasZmanKidushLevana3Days + "\"")
                    .add("\"" + tchilasZmanKidushLevana7Days + "\"")
                    .add("\"" + sofZmanKidushLevanaBetweenMoldos + "\"")
                    .add("\"" + sofZmanKidushLevana15Days + "\"")
                    .add("\"" + tekufasTishreiElapsedDays + "\"")
                    .toString();
        }

        private final LocalDate current;
        private final JewishDate currentJewishDate;
        private final int yomTovIndex;
        private final Daf dafYomiBavli;
        private final Daf dafYomiYerushalmi;
        private final boolean isruChag;
        private final boolean birkasHachamah;
        private final JewishCalendar.Parsha parshah;
        private final JewishCalendar.Parsha upcomingParshah;
        private final JewishCalendar.Parsha specialShabbos;
        private final boolean yomTov;
        private final boolean yomTovAssurBemelacha;
        private final boolean assurBemelacha;
        private final boolean hasCandleLighting;
        private final boolean tomorrowShabbosOrYomTov;
        private final boolean erevYomTovSheni;
        private final boolean aseresYemeiTeshuva;
        private final boolean pesach;
        private final boolean cholHamoedPesach;
        private final boolean shavuos;
        private final boolean roshHashana;
        private final boolean yomKippur;
        private final boolean succos;
        private final boolean hoshanaRabba;
        private final boolean shminiAtzeres;
        private final boolean simchasTorah;
        private final boolean cholHamoedSuccos;
        private final boolean cholHamoed;
        private final boolean erevYomTov;
        private final boolean erevRoshChodesh;
        private final boolean yomKippurKatan;
        private final boolean beHaB;
        private final boolean taanis;
        private final boolean taanisBechoros;
        private final int dayOfChanukah;
        private final boolean chanukah;
        private final boolean purim;
        private final boolean roshChodesh;
        private final boolean macharChodesh;
        private final boolean shabbosMevorchim;
        private final int dayOfOmer;
        private final boolean tishaBav;
        private final JewishDate molad;
        private final Date moladAsDate;
        private final Date tchilasZmanKidushLevana3Days;
        private final Date tchilasZmanKidushLevana7Days;
        private final Date sofZmanKidushLevanaBetweenMoldos;
        private final Date sofZmanKidushLevana15Days;
        private final int tekufasTishreiElapsedDays;

        public FullCalendar(LocalDate current, JewishDate currentJewishDate, int yomTovIndex, Daf dafYomiBavli, Daf dafYomiYerushalmi, boolean isruChag, boolean birkasHachamah, JewishCalendar.Parsha parshah, JewishCalendar.Parsha upcomingParshah, JewishCalendar.Parsha specialShabbos, boolean yomTov, boolean yomTovAssurBemelacha, boolean assurBemelacha, boolean hasCandleLighting, boolean tomorrowShabbosOrYomTov, boolean erevYomTovSheni, boolean aseresYemeiTeshuva, boolean pesach, boolean cholHamoedPesach, boolean shavuos, boolean roshHashana, boolean yomKippur, boolean succos, boolean hoshanaRabba, boolean shminiAtzeres, boolean simchasTorah, boolean cholHamoedSuccos, boolean cholHamoed, boolean erevYomTov, boolean erevRoshChodesh, boolean yomKippurKatan, boolean beHaB, boolean taanis, boolean taanisBechoros, int dayOfChanukah, boolean chanukah, boolean purim, boolean roshChodesh, boolean macharChodesh, boolean shabbosMevorchim, int dayOfOmer, boolean tishaBav, JewishDate molad, Date moladAsDate, Date tchilasZmanKidushLevana3Days, Date tchilasZmanKidushLevana7Days, Date sofZmanKidushLevanaBetweenMoldos, Date sofZmanKidushLevana15Days, int tekufasTishreiElapsedDays) {
            this.current = current;
            this.currentJewishDate = currentJewishDate;
            this.yomTovIndex = yomTovIndex;
            this.dafYomiBavli = dafYomiBavli;
            this.dafYomiYerushalmi = dafYomiYerushalmi;
            this.isruChag = isruChag;
            this.birkasHachamah = birkasHachamah;
            this.parshah = parshah;
            this.upcomingParshah = upcomingParshah;
            this.specialShabbos = specialShabbos;
            this.yomTov = yomTov;
            this.yomTovAssurBemelacha = yomTovAssurBemelacha;
            this.assurBemelacha = assurBemelacha;
            this.hasCandleLighting = hasCandleLighting;
            this.tomorrowShabbosOrYomTov = tomorrowShabbosOrYomTov;
            this.erevYomTovSheni = erevYomTovSheni;
            this.aseresYemeiTeshuva = aseresYemeiTeshuva;
            this.pesach = pesach;
            this.cholHamoedPesach = cholHamoedPesach;
            this.shavuos = shavuos;
            this.roshHashana = roshHashana;
            this.yomKippur = yomKippur;
            this.succos = succos;
            this.hoshanaRabba = hoshanaRabba;
            this.shminiAtzeres = shminiAtzeres;
            this.simchasTorah = simchasTorah;
            this.cholHamoedSuccos = cholHamoedSuccos;
            this.cholHamoed = cholHamoed;
            this.erevYomTov = erevYomTov;
            this.erevRoshChodesh = erevRoshChodesh;
            this.yomKippurKatan = yomKippurKatan;
            this.beHaB = beHaB;
            this.taanis = taanis;
            this.taanisBechoros = taanisBechoros;
            this.dayOfChanukah = dayOfChanukah;
            this.chanukah = chanukah;
            this.purim = purim;
            this.roshChodesh = roshChodesh;
            this.macharChodesh = macharChodesh;
            this.shabbosMevorchim = shabbosMevorchim;
            this.dayOfOmer = dayOfOmer;
            this.tishaBav = tishaBav;
            this.molad = molad;
            this.moladAsDate = moladAsDate;
            this.tchilasZmanKidushLevana3Days = tchilasZmanKidushLevana3Days;
            this.tchilasZmanKidushLevana7Days = tchilasZmanKidushLevana7Days;
            this.sofZmanKidushLevanaBetweenMoldos = sofZmanKidushLevanaBetweenMoldos;
            this.sofZmanKidushLevana15Days = sofZmanKidushLevana15Days;
            this.tekufasTishreiElapsedDays = tekufasTishreiElapsedDays;
        }
    }
}
