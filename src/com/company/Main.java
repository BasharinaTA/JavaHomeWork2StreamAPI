package com.company;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Patient> patients = Dump.getDump();

        System.out.print(getMapWithPatient((patients)));
        System.out.print(getMap((patients)));
        listExpenses(patients);
        System.out.print(getMinPatient((patients)));
        System.out.print(getMaxPatient((patients)));
        System.out.println(getPatientFirstDecember((patients)));
        System.out.println(hasAbsolutelyHealthy((patients)));
        System.out.println(hasOlder100YearsNone((patients)));
        System.out.println(hasOlder100YearsAny((patients)));
    }

    // Преобразовать стрим из пациентов в Map, где "ключ" дата рождения, а значение - фио без преобразования
    public static Map<LocalDate, String> getMapWithPatient(List<Patient> list) {
        return list.stream()
                .collect(Collectors.toMap(Patient::getBirthDate,
                        Patient::getFio,  (k1, k2) -> k1));
    }

    public static Map<LocalDate, List<String>> getMap(List<Patient> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Patient::getBirthDate,
                        Collectors.mapping(Patient::getFio, Collectors.toList())));
    }

    // Вывести на экран только список расходов каждого пациента
    public static void listExpenses(List<Patient> list) {
        list.stream()
                .map(Patient::getExpenses)
                .forEach(System.out::println);
    }

    // Получить "минимального" пациента из потока
    public static Patient getMinPatient(List<Patient> list) {
        return list.stream()
                .min(Comparator.comparingInt(p -> p.getExpenses().stream().reduce(0, Integer::sum))).orElse(null);
    }

    // Получить "максимального" пациента из потока
    public static Patient getMaxPatient(List<Patient> list) {
        return list.stream()
                .max(Comparator.comparingInt(p -> p.getExpenses().stream().reduce(0, Integer::sum))).orElse(null);
    }

    // Получить первого пациента, кто родился в декабре 1999 года
    public static Patient getPatientFirstDecember(List<Patient> list) {
        return list.stream()
                .filter(p -> p.getBirthDate().getMonth() == Month.DECEMBER && p.getBirthDate().getYear() == 1999)
                .findFirst().orElse(null);
    }

    // Проверить, есть ли хоть один АБСОЛЮТНО ЗДОРОВЫЙ человек
    public static boolean hasAbsolutelyHealthy(List<Patient> list) {
        return !list.stream()
                .allMatch(p -> p.getExpenses().size() > 0);
    }

    // Проверить. есть ли хоть один человек, старше 100 лет
    public static boolean hasOlder100YearsNone(List<Patient> list) {
        return !list.stream()
                .noneMatch(p -> Period.between(p.getBirthDate(), LocalDate.now()).getYears()  > 100);
    }

    public static boolean hasOlder100YearsAny(List<Patient> list) {
        return list.stream()
                .anyMatch(p -> Period.between(p.getBirthDate(), LocalDate.now()).getYears() > 100);
    }
}
