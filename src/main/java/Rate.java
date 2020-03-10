import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
    public class Rate {

        @JsonProperty("no")
        public String no;
        @JsonProperty("effectiveDate")
        public String effectiveDate;
        @JsonProperty("mid")
        public Float mid;

    }

