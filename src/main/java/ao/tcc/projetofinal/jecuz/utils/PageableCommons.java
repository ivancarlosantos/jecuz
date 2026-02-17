package ao.tcc.projetofinal.jecuz.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableCommons<CONTENT> {

    private CONTENT content;
    private int pageNumber;
    private int numberContent;
    private int totalPages;
    private long totalElements;
}
