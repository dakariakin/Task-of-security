import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ControlSumm {
    public void setOriginalControlSumm(String file) {
        this.originalControlSumm = checkControlSumm(file);
    }

    private String originalControlSumm;

    public ArrayList<String> findFile(String path) {
        String[] dirForSearch = new File(path).list();
        ArrayList<String> result = new ArrayList<String>();
        for (String current : dirForSearch) {
            String currentPath = path.concat(current);
            File currentFile = new File(currentPath);
            if (currentFile.isDirectory()) {
                result.addAll(findFile(currentPath));
            } else {
                if (checkControlSumm(currentPath).equals(this.originalControlSumm)) {
                    result.add(currentPath);
                }
            }
        }

        return result;
    }

    private String checkControlSumm(String file) {
        byte[] buf = new byte[8000];
        int nLength = 0;
        Checksum cs = new CRC32();

        try {
            FileInputStream inputFile = new FileInputStream(file);

            while ((nLength = inputFile.read(buf)) >= 0) {
                cs.update(buf, 0, nLength);
            }

            inputFile.close();
        } catch (FileNotFoundException notFound) {
            //скорее всего, в имени файла пробел, а с такими мы не работаем
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Long.toString(cs.getValue());
    }
}
