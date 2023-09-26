/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.lang.Nullable;

/**
 * 它的 BeanPostProcessor 子接口添加实例化前的回调，以及实例化之后但在设置显式属性或自动连线之前出现的回调。
 * 通常用于抑制特定目标 Bean 的默认实例化，例如，使用特殊 TargetSources 创建代理（池化目标、延迟初始化目标等），或实现其他注入策略，如字段注入。
 * 注意： 此接口是一个专用接口，主要用于框架内部使用。建议尽可能实现普通 BeanPostProcessor 接口，或者派生自 InstantiationAwareBeanPostProcessorAdapter ，以便屏蔽对此接口的扩展
 * <p>
 * <p>
 * InstantiationAwareBeanPostProcessor 是 Spring 框架中的一种特殊类型的 Bean 后处理器，它提供了自定义 Spring Bean 实例化和初始化过程的钩子或回调方法。它允许您介入 Bean 创建的生命周期，自定义 Bean 的创建和配置方式。
 * <p>
 * InstantiationAwareBeanPostProcessor 主要包含以下方法：
 * <p>
 * postProcessBeforeInstantiation：该方法在实际实例化 Bean 之前调用。它提供了在 Bean 创建过程中进行干预的机会，您可以在此方法中返回自定义的对象或返回 null，以指示正常的实例化过程是否应继续。
 * <p>
 * postProcessAfterInstantiation：该方法在 Bean 实例化之后但在进行属性注入之前调用。您可以在此方法中进一步自定义 Bean，例如，您可以修改实例化后的 Bean 对象。
 * <p>
 * postProcessPropertyValues：该方法在 Bean 的属性值注入之后被调用。您可以在此方法中对 Bean 的属性进行额外的定制或验证。
 * <p>
 * 这些方法允许您在 Spring 容器创建和初始化 Bean 时介入并执行自定义逻辑，以满足特定需求。通常，您可以通过实现 InstantiationAwareBeanPostProcessor 接口并将其注册到 Spring 容器中来使用这些方法。
 * <p>
 * Subinterface of {@link BeanPostProcessor} that adds a before-instantiation callback,
 * and a callback after instantiation but before explicit properties are set or
 * autowiring occurs.
 *
 * <p>Typically used to suppress default instantiation for specific target beans,
 * for example to create proxies with special TargetSources (pooling targets,
 * lazily initializing targets, etc), or to implement additional injection strategies
 * such as field injection.
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. It is recommended to implement the plain
 * {@link BeanPostProcessor} interface as far as possible, or to derive from
 * {@link InstantiationAwareBeanPostProcessorAdapter} in order to be shielded
 * from extensions to this interface.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#setCustomTargetSourceCreators
 * @see org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator
 * @since 1.2
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 在实例化目标 Bean 之前应用此 BeanPostProcessor。返回的 Bean 对象可以是要使用的代理而不是目标 Bean，从而有效地抑制了目标 Bean 的默认实例化。
	 * 如果此方法返回非 null 对象，则 Bean 创建过程将短路。应用的唯一进一步处理是 postProcessAfterInitialization 来自已配置 BeanPostProcessors的 .
	 * 此回调将仅应用于具有 Bean 类的 Bean 定义。特别是，它不适用于工厂方法的豆类。
	 * 后处理器可以实现扩展 SmartInstantiationAwareBeanPostProcessor 接口，以便预测它们将在此处返回的 Bean 对象的类型。
	 * 默认实现返回 null。
	 * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
	 * The returned bean object may be a proxy to use instead of the target bean,
	 * effectively suppressing default instantiation of the target bean.
	 * <p>If a non-null object is returned by this method, the bean creation process
	 * will be short-circuited. The only further processing applied is the
	 * {@link #postProcessAfterInitialization} callback from the configured
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>This callback will only be applied to bean definitions with a bean class.
	 * In particular, it will not be applied to beans with a factory method.
	 * <p>Post-processors may implement the extended
	 * {@link SmartInstantiationAwareBeanPostProcessor} interface in order
	 * to predict the type of the bean object that they are going to return here.
	 * <p>The default implementation returns {@code null}.
	 *
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName  the name of the bean
	 * @return the bean object to expose instead of a default instance of the target bean,
	 * or {@code null} to proceed with default instantiation
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessAfterInstantiation
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#hasBeanClass
	 */
	@Nullable
	default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * 在通过构造函数或工厂方法实例化 Bean 之后，但在 Spring 属性填充（来自显式属性或自动连线）发生之前执行操作。
	 * 这是在给定的 bean 实例上执行自定义字段注入的理想回调，就在 Spring 的自动连线启动之前。
	 * 默认实现返回 true
	 * Perform operations after the bean has been instantiated, via a constructor or factory method,
	 * but before Spring property population (from explicit properties or autowiring) occurs.
	 * <p>This is the ideal callback for performing custom field injection on the given bean
	 * instance, right before Spring's autowiring kicks in.
	 * <p>The default implementation returns {@code true}.
	 *
	 * @param bean     the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return {@code true} if properties should be set on the bean; {@code false}
	 * if property population should be skipped. Normal implementations should return {@code true}.
	 * Returning {@code false} will also prevent any subsequent InstantiationAwareBeanPostProcessor
	 * instances being invoked on this bean instance.
	 * 如果应该在bean上设置属性，则为True; 如果应该跳过属性设置，则为False。正常的实现应该返回true。
	 * 返回false还将阻止在此bean实例上调用任何后续的InstantiationAwareBeanPostProcessor实例。
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessBeforeInstantiation
	 */
	default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}

	/**
	 * 在工厂将给定的属性值应用于给定的 Bean 之前对其进行后处理，而无需任何属性描述符。
	 * 如果实现提供自定义postProcessPropertyValues实现pvs，则应返回（默认值），否则应返回null。
	 * 在此接口的未来版本中（已删除postProcessPropertyValues），默认实现将直接返回给定pvs的原样。
	 * Post-process the given property values before the factory applies them
	 * to the given bean, without any need for property descriptors.
	 * <p>Implementations should return {@code null} (the default) if they provide a custom
	 * {@link #postProcessPropertyValues} implementation, and {@code pvs} otherwise.
	 * In a future version of this interface (with {@link #postProcessPropertyValues} removed),
	 * the default implementation will return the given {@code pvs} as-is directly.
	 *
	 * @param pvs      the property values that the factory is about to apply (never {@code null})
	 * @param bean     the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} which proceeds with the existing properties
	 * but specifically continues with a call to {@link #postProcessPropertyValues}
	 * (requiring initialized {@code PropertyDescriptor}s for the current bean class)
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessPropertyValues
	 * @since 5.1
	 */
	@Nullable
	default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>Also allows for replacing the property values to apply, typically through
	 * creating a new MutablePropertyValues instance based on the original PropertyValues,
	 * adding or removing specific values.
	 * <p>The default implementation returns the given {@code pvs} as-is.
	 *
	 * @param pvs      the property values that the factory is about to apply (never {@code null})
	 * @param pds      the relevant property descriptors for the target bean (with ignored
	 *                 dependency types - which the factory handles specifically - already filtered out)
	 * @param bean     the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} to skip property population
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessProperties
	 * @see org.springframework.beans.MutablePropertyValues
	 * @deprecated as of 5.1, in favor of {@link #postProcessProperties(PropertyValues, Object, String)}
	 */
	@Deprecated
	@Nullable
	default PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

		return pvs;
	}

}
