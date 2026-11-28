package tfar.slowerexploration.platform;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import org.jspecify.annotations.Nullable;
import tfar.slowerexploration.attachments.CommonDataAttachment;
import tfar.slowerexploration.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.builder();
        if (attachment.isCopyOnDeath()) {
            builder.copyOnDeath();
        }
        builder.initializer(() -> attachment.getDefaultValueSupplier().apply(null));
        if (attachment.getCodec() != null) {
            builder.persistent(attachment.getCodec());
        }
        if (attachment.canSync()) {
            builder.syncWith(attachment.getStreamCodec(),(_, _) -> true);//who to notify of value
        }
        AttachmentType<T> attachmentType = builder.buildAndRegister(attachment.getName());
        attachment.setAttachment(attachmentType);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public @Nullable <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof AttachmentTarget attachmentTarget) {
            return attachmentTarget.getAttached(type);
        }
        throw new RuntimeException(object +" does not support attachments!");
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, T value) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof AttachmentTarget attachmentTarget) {
            attachmentTarget.setAttached(type, value);
        } else {
            throw new RuntimeException(object + " does not support attachments!");
        }
    }
}
