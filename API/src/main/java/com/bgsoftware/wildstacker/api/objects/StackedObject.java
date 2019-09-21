package com.bgsoftware.wildstacker.api.objects;

import com.bgsoftware.wildstacker.api.enums.StackCheckResult;
import com.bgsoftware.wildstacker.api.enums.StackResult;
import com.bgsoftware.wildstacker.api.enums.UnstackResult;
import org.bukkit.Location;

import java.util.Optional;
import java.util.function.Consumer;

public interface StackedObject<T> {

    /**
     * Get the location of the object.
     */
    Location getLocation();

    /**
     * Get the stack amount
     * @return stack amount
     */
    int getStackAmount();

    /**
     * Update the stack amount
     * @param stackAmount a new stack amount
     * @param updateName should the name of the object get updated or not
     */
    void setStackAmount(int stackAmount, boolean updateName);

    /**
     * Get the stack limit
     * @return stack limit
     */
    int getStackLimit();

    /**
     * Checks if an object is blacklisted.
     * @return True if the object is blacklisted, otherwise false.
     */
    boolean isBlacklisted();

    /**
     * Checks if an object is whitelisted.
     * @return True if the object is whitelisted, otherwise false.
     */
    boolean isWhitelisted();

    /**
     * Checks if the world of the object is disabled.
     * @return True if the world is disabled, otherwise false.
     */
    boolean isWorldDisabled();

    /**
     * Remove the stack object from cache and the server
     */
    void remove();

    /**
     * Update the name of the object.
     */
    void updateName();

    /**
     * Checks if this object can stack into another object.
     * @param stackedObject other object to check
     *
     * @deprecated See runStackCheck
     */
    @Deprecated
    boolean canStackInto(StackedObject stackedObject);

    /**
     * Checks if this object can stack into another object.
     * @param stackedObject other object to check
     * @return The result for the operation.
     */
    StackCheckResult runStackCheck(StackedObject stackedObject);

    /**
     * Stack this object into other objects around it.
     * @param result The object that this object was stacked into.
     */
    void runStackAsync(Consumer<Optional<T>> result);

    /**
     * Stack this object into other objects around it.
     * @return The object that this object was stacked into (nullable)
     *
     * @deprecated See runStackAsync(Consumer)
     */
    @Deprecated
    T tryStack();

    /**
     * Stack this object into another object.
     * @param stackedObject another object to stack into
     * @param stackResult The result for the stacking operation.
     */
    void runStackAsync(StackedObject stackedObject, Consumer<StackResult> stackResult);

    /**
     * Stack this object into another object.
     * !Usage of this method can cause issues!
     * @param stackedObject another object to stack into
     * @return The result for the stacking operation.
     */
    StackResult runStack(StackedObject stackedObject);

    /**
     * Stack this object into another object.
     * @param stackedObject another object to stack into
     * @return True if success, otherwise false.
     *
     * @deprecated See runStackAsync(StackedObject, Consumer)
     */
    @Deprecated
    boolean tryStackInto(StackedObject stackedObject);

    /**
     * Unstack this object.
     * @param amount unstack by this amount
     * @return The result for the unstacking operation.
     */
    UnstackResult runUnstack(int amount);

    /**
     * Unstack this object.
     * @param amount unstack by this amount
     * @return True if success, otherwise false.
     *
     * @deprecated See runUnstack
     */
    @Deprecated
    boolean tryUnstack(int amount);

    /**
     * Checks if this object is similar to another object.
     * @param stackedObject the object to check
     * @return True if they are similar objects, otherwise false
     */
    boolean isSimilar(StackedObject stackedObject);

}
