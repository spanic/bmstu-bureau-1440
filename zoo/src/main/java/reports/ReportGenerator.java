package reports;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ReportGenerator {

    @Setter
    ProvidesInfo infoProvider;

    public void generateReport() {
        if (infoProvider == null) {
            throw new IllegalStateException("Info provider is not set");
        }
        infoProvider.provideInfo();
    }

}
