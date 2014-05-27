package org.gridgain.grid.kernal.processors.rest.client.message;

import org.gridgain.grid.util.direct.*;

import java.nio.*;
import java.util.*;

/**
 * Client message wrapper for direct marshalling.
 */
public class GridClientMessageWrapper extends GridTcpCommunicationMessageAdapter {
    /** Client request header. */
    public static final byte REQ_HEADER = (byte)0x90;

    /** */
    private int msgSize;

    /** */
    private long reqId;

    /** */
    private UUID clientId;

    /** */
    private UUID destId;

    /** */
    private byte[] msg;

    /**
     * @return Request ID.
     */
    public long requestId() {
        return reqId;
    }

    /**
     * @param reqId Request ID.
     */
    public void requestId(long reqId) {
        this.reqId = reqId;
    }

    /**
     * @return Message size.
     */
    public int messageSize() {
        return msgSize;
    }

    /**
     * @param msgSize Message size.
     */
    public void messageSize(int msgSize) {
        this.msgSize = msgSize;
    }

    /**
     * @return Client ID.
     */
    public UUID clientId() {
        return clientId;
    }

    /**
     * @param clientId Client ID.
     */
    public void clientId(UUID clientId) {
        this.clientId = clientId;
    }

    /**
     * @return Destination ID.
     */
    public UUID destinationId() {
        return destId;
    }

    /**
     * @param destId Destination ID.
     */
    public void destinationId(UUID destId) {
        this.destId = destId;
    }

    /**
     * @return Message bytes.
     */
    public byte[] message() {
        return msg;
    }

    /**
     * @param msg Message bytes.
     */
    public void message(byte[] msg) {
        this.msg = msg;
    }

    /** {@inheritDoc} */
    @Override public boolean writeTo(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!commState.typeWritten) {
            if (!commState.putByte(directType()))
                return false;

            commState.typeWritten = true;
        }

        switch (commState.idx) {
            case 0:
                if (!commState.putIntClient(msgSize))
                    return false;

                commState.idx++;

            case 1:
                if (!commState.putLongClient(reqId))
                    return false;

                commState.idx++;

            case 2:
                if (!commState.putUuidClient(clientId))
                    return false;

                commState.idx++;

            case 3:
                if (!commState.putUuidClient(destId))
                    return false;

                commState.idx++;

            case 4:
                if (!commState.putByteArrayClient(msg))
                    return false;

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public boolean readFrom(ByteBuffer buf) {
        commState.setBuffer(buf);

        switch (commState.idx) {
            case 0:
                if (buf.remaining() < 4)
                    return false;

                msgSize = commState.getIntClient();

                commState.idx++;

            case 1:
                if (buf.remaining() < 8)
                    return false;

                reqId = commState.getLongClient();

                commState.idx++;

            case 2:
                UUID clientId0 = commState.getUuidClient();

                if (clientId0 == UUID_NOT_READ)
                    return false;

                clientId = clientId0;

                commState.idx++;

            case 3:
                UUID destId0 = commState.getUuidClient();

                if (destId0 == UUID_NOT_READ)
                    return false;

                destId = destId0;

                commState.idx++;

            case 4:
                byte[] msg0 = commState.getByteArrayClient(msgSize - 40);

                if (msg0 == BYTE_ARR_NOT_READ)
                    return false;

                msg = msg0;

                commState.idx++;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return REQ_HEADER;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneCallsConstructors"})
    @Override public GridTcpCommunicationMessageAdapter clone() {
        GridClientMessageWrapper _clone = new GridClientMessageWrapper();

        clone0(_clone);

        return _clone;
    }

    /** {@inheritDoc} */
    @Override protected void clone0(GridTcpCommunicationMessageAdapter _msg) {
        GridClientMessageWrapper _clone = (GridClientMessageWrapper)_msg;

        _clone.reqId = reqId;
        _clone.msgSize = msgSize;
        _clone.clientId = clientId;
        _clone.destId = destId;
        _clone.msg = msg;
    }
}
