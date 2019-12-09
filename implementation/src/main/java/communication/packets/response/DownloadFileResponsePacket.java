package communication.packets.response;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadFileResponsePacket extends ResponsePacket {

    @Expose
    private byte[] fileBytes;
    @Expose
    private String fileName;

    public DownloadFileResponsePacket(File file, String fileName) {
        super(PacketType.DOWNLOAD_FILE_REQUEST, RequestResult.Valid);
        fileBytes = new byte[(int) file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileBytes); //read file into bytes[]
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.fileName = fileName;
    }
}
