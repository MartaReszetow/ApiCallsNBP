import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TableTypeB {

    @JsonProperty("table")
    public String table;
    @JsonProperty("no")
    public String no;
    @JsonProperty("effectiveDate")
    public String effectiveDate;
    @JsonProperty("rates")
    public List<RateB> ratesB =null;
}
