import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
    public class RateB {

    @JsonProperty("currency")
    public String currency;
    @JsonProperty("code")
    public String code;
    @JsonProperty("mid")
    public Float mid;


    }

