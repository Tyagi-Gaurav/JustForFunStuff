package com.gt.functional.todolist

/*
    Wrap all the domain logic and keep it separated from the external adapter.
    This approach is called Port and Adapter.
    Also known as Hexagonal Architecture.
    The primary goal of this pattern is to create a separation between the core business logic of a system
    and the external dependencies it relies on, such as databases, web services, and user interfaces.

    This domain-wrapping interface works like a hub, since it stays in the center of our application,
    and it’s connected to the external by many functions that work like the spokes of a wheel.
 */
interface ZettaiHub {
    fun getList(user: User, list: ListName) : TodoList?
}