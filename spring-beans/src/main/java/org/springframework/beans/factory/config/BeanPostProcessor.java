/*
 * Copyright 2002-2016 the original author or authors.
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

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * 工厂钩子，允许对新的 Bean 实例进行自定义修改，例如检查标记接口或用代理包装它们。
 * ApplicationContexts 可以在其 Bean 定义中自动检测 BeanPostProcessor bean，并将它们应用于随后创建的任何 bean。普通 Bean 工厂允许以编程方式注册后处理器，适用于通过此工厂创建的所有 Bean。
 * 通常，通过标记接口等填充 Bean 的后处理器将实现 postProcessBeforeInitialization，而使用代理包装 Bean 的后处理器通常会实现 postProcessAfterInitialization。
 *
 * Factory hook that allows for custom modification of new bean instances,
 * e.g. checking for marker interfaces or wrapping them with proxies.
 *
 * <p>ApplicationContexts can autodetect BeanPostProcessor beans in their
 * bean definitions and apply them to any beans subsequently created.
 * Plain bean factories allow for programmatic registration of post-processors,
 * applying to all beans created through this factory.
 *
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 *
 * @author Juergen Hoeller
 * @since 10.10.2003
 * @see InstantiationAwareBeanPostProcessor
 * @see DestructionAwareBeanPostProcessor
 * @see ConfigurableBeanFactory#addBeanPostProcessor
 * @see BeanFactoryPostProcessor
 */
public interface BeanPostProcessor {

	/**
	 * 在任何 Bean 初始化回调（如 InitializingBean afterPropertiesSet 或自定义 init-method）之前，将此 BeanPostProcessor 应用于给定的新 Bean 实例。
	 * Bean 将已填充了属性值。返回的 Bean 实例可能是原始实例的包装器。
	 * 默认实现按原样返回给定 bean 的原样。
	 * Apply this BeanPostProcessor to the given new bean instance <i>before</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 */
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 *
	 * 在任何 Bean 初始化回调（如 InitializingBean afterPropertiesSet 或自定义 init-method）之后，将此 BeanPostProcessor 应用于给定的新 Bean 实例。
	 * Bean 中已填充了属性值。返回的 Bean 实例可能是原始实例的包装器。
	 * 如果是 FactoryBean，则将为 FactoryBean 实例和 FactoryBean 创建的对象调用此回调（从 Spring 2.0 开始）。
	 * 后处理器可以通过相应的 bean instanceof FactoryBean 检查来决定是应用于 FactoryBean 还是创建的对象，或者两者兼而有之。
	 * 与所有其他 BeanPostProcessor 回调相比，此回调也将在方法触发 InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation 短路后调用。
	 * 默认实现按原样返回给定 bean 的。
	 * Apply this BeanPostProcessor to the given new bean instance <i>after</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
	 * instance and the objects created by the FactoryBean (as of Spring 2.0). The
	 * post-processor can decide whether to apply to either the FactoryBean or created
	 * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
	 * <p>This callback will also be invoked after a short-circuiting triggered by a
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
	 * in contrast to all other BeanPostProcessor callbacks.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.FactoryBean
	 */
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
