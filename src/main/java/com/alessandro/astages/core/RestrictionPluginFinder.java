package com.alessandro.astages.core;

import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.store.AttributeType;
import com.alessandro.astages.util.AClientRestrictionSynchronizer;
import com.google.common.reflect.TypeToken;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Deprecated(forRemoval = true)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RestrictionPluginFinder {
    public static final AttributeType<ARestriction<?, ?, ?>> RESTRICTION_TYPE = AttributeType.create(new TypeToken<>() { });

    public RestrictionPluginFinder() { }

    public static List<ARestriction<?, ?, ?>> getAClientRestrictionSynchronizer() {
    // public static Set<String> getAClientRestrictionSynchronizer() {
        Type annotationType = Type.getType(AClientRestrictionSynchronizer.class);
        List<ModFileScanData> allData = ModList.get().getAllScanData();
        Set<String> allClassesName = new HashSet<>();

        // Get all classes as string with specific annotation
        for (var data : allData) {
            for (var annotation : data.getAnnotations()) {
                if (annotation.annotationType().equals(annotationType)) {
                    allClassesName.add(annotation.memberName());
                }
            }
        }



        // return allClassesName;

        List<ARestriction<?, ?, ?>> classes = new ArrayList<>();
        for (var name : allClassesName) {
//            try {
//                Class<?> asmClass = Class.forName(name);
//                Class<? extends ARestriction<?, ?, ?>> asmInstanceClass = asmClass.asSubclass(RESTRICTION_TYPE.getType());
//                Constructor<? extends ARestriction<?, ?, ?>> constructor = asmInstanceClass.getMethod();
//                ARestriction<?, ?, ?> newInstance = constructor.newInstance();
//                classes.add(newInstance);

//            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
//                     InstantiationException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
        }

        return classes;
    }
}
