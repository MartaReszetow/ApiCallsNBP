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

public class CurrencyData {
    @JsonProperty("table")
    public String table;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("code")
    public String code;
    @JsonProperty("rates")
    public List<Rate> rates = null;
}
