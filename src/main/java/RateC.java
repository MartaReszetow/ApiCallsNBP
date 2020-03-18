import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor

    public class RateC {


    @JsonProperty("no")
    public String no;
    @JsonProperty("effectiveDate")
    public String effectiveDate;
    @JsonProperty("bid")
    public Float bid;
    @JsonProperty("ask")
    public Float ask;


    @Override
    public String toString() {
        return "{" +
                "data='" + effectiveDate + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}

