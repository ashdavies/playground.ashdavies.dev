package dev.ashdavies.sql

import app.cash.sqldelight.SuspendingTransacter

public interface DatabaseFactory<T : SuspendingTransacter> : Suspended<T>
