package com.justin.contrast.metric.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

class CountingServletOutputStream extends ServletOutputStream {
    private final ServletOutputStream os;
    private long bytesWritten;

    CountingServletOutputStream(final ServletOutputStream os) {
        this.os = os;
    }

    @Override
    public boolean isReady() {
        return os.isReady();
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {
        os.setWriteListener(writeListener);
    }

    @Override
    public void write(final int b)
            throws IOException {
        os.write(b);
        bytesWritten++;
    }

    long getBytesWritten() {
        return bytesWritten;
    }
}
