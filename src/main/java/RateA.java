import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor

    public class RateA {

        @JsonProperty("no")
        public String no;
        @JsonProperty("effectiveDate")
        public String effectiveDate;
        @JsonProperty("mid")
        public Float mid;

    @Override
    public String toString() {
        return "{" +
                "data='" + effectiveDate + '\'' +
                ", mid=" + mid +
                '}';
    }
}

