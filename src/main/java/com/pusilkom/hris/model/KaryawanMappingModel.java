package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaryawanMappingModel implements Comparable<KaryawanMappingModel> {
    private KaryawanModel karyawan;
    private List<RekapModel> rekapList;

    public int getTotal() {
        int total = 0;

        for(RekapModel rekap : rekapList) {
            total += (int) (rekap.getPersentaseKontribusi()*100);
        }

        return total;
    }

    public int compareTo(KaryawanMappingModel otherMapping) {
        return otherMapping.getTotal() - getTotal();
    }

}
