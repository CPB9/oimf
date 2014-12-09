namespace com::cpb9::test::clock {

typedef oimf_i32 clock_time_diff;

oimf_i32 clock_time_diff_get_seconds(clock_time_diff self);
oimf_i32 clock_time_diff_get_minutes(clock_time_diff self);
oimf_i32 clock_time_diff_get_hours(clock_time_diff self);
oimf_i32 clock_time_diff_get_days(clock_time_diff self);
oimf_i32 clock_time_diff_get_months(clock_time_diff self);
oimf_i32 clock_time_diff_get_years(clock_time_diff self);

typedef uint32_t oimf_u32;
typedef uint8_t oimf_u8;
typedef oimf_u8 oimf_boolean;
typedef oimf_u32 clock_time;

oimf_u32 clock_time_get_second(clock_time self);
oimf_u32 clock_time_get_minute(clock_time self);
oimf_u32 clock_time_get_hour(clock_time self);
oimf_u32 clock_time_get_day_of_month(clock_time self);
oimf_u32 clock_time_get_year(clock_time self);

const oimf_size CLOCK_CLOCK_SIZE = 5;

enum clock_cmd { CLOCK_CLOCK_CMD_SET_TIME, CLOCK_CLOCK_CMD_ADVANCE_SECOND, CLOCK_CLOCK_CMD_ADVANCE_MINUTE,
    CLOCK_CLOCK_CMD_COMPUTE_DIFF_WITH };

enum clock_tm { CLOCK_CLOCK_TM_CURRENT_UTC_TIME, CLOCK_CLOCK_TM_IS_READY, CLOCK_CLOCK_TM_CMD_RESULT };

class clock {
private:
    clock_time _current_utc_time;
    oimf_boolean _is_ready;
public:
    void set_current_utc_time(clock_time current_utc_time) {
        this->current_utc_time = current_utc_time;
    };
    void set_is_ready(oimf_boolean is_ready) {
        this->is_ready = is_ready;
    }
    void set_time_impl(clock_time new_time);
    void advance_second_impl();
    void advance_minute_impl();
    clock_time_diff compute_diff_with_impl(clock_time time);

    oimf_cmd_result set_time(oimf_reader* reader) {
        clock_time new_time;
        if (reader->avail_size() < sizeof(clock_time)) {
            return OIMF_CMD_NOT_ENOUGH_PARAMS_SIZE;
        }
        reader->read_u32((oimf_u32*) &new_time);
        return set_time_impl(new_time);
    };

    oimf_cmd_result compute_diff_with(oimf_stack* stack) {
        clock_time time;
        if (stack->avail_size() < sizeof(clock_time)) {
            return OIMF_CMD_NOT_ENOUGH_PARAMS_SIZE;
        }
        time = (clock_time) stack->pop_u32();
        stack->push_i32((oimf_i32) compute_diff_with_impl(self, time));
        return OIMF_CMD_OK;
    };

    oimf_cmd_result command_execute(oimf_stack* stack) {
        if (stack->avail_size() < 1) {
            return OIMF_CMD_NOT_ENOUGH_PARAMS_SIZE;
        }
        oimf_u8 = cmd_num = stack->pop_u8();
        switch (cmd_num) {
            case 0:
                return set_time(stack);
            case 1:
                return advance_second_impl();
            case 2:
                return advance_minute_impl();
            case 3:
                return compute_diff_with(stack);
            default:
                return OIMF_CMD_INVALID_COMMAND_NUM;
        }
    };

    oimf_tm_result write_current_utc_time(oimf_writer *writer) {
        if (writer->avail_size() < sizeof(clock_time)) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u32(_current_utc_time);
        return OIMF_TM_OK;
    };

    oimf_tm_result write_is_ready(oimf_writer *writer) {
        if (writer->avail_size() < sizeof(oimf_boolean)) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(_is_ready);
        return OIMF_TM_OK;
    };

    oimf_tm_result write_set_time_cmd_result(clock_clock new_time, oimf_writer *writer) {
        if (writer->avail_size() < 2 + sizeof(clock_clock_time)) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(CLOCK_CLOCK_TM_CMD_RESULT);
        writer->write_u8(CLOCK_CLOCK_CMD_SET_TIME);
        return OIMF_TM_OK;
    };

    oimf_tm_result write_advance_second_cmd_result(oimf_writer *writer) {
        if (writer->avail_size() < 2) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(CLOCK_CLOCK_TM_CMD_RESULT);
        writer->write_u8(CLOCK_CLOCK_CMD_ADVANCE_SECOND);
        return OIMF_TM_OK;
    };

    oimf_tm_result write_advance_minute_cmd_result(oimf_writer *writer) {
        if (writer->avail_size() < 2) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(CLOCK_CLOCK_TM_CMD_RESULT);
        writer->write_u8(CLOCK_CLOCK_CMD_ADVANCE_MINUTE);
        return OIMF_TM_OK;
    };

    oimf_tm_result write_compute_diff_with_cmd_result(clock_time_diff result, oimf_writer *writer) {
        if (writer->avail_size() < 2 + sizeof(clock_time_diff)) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(CLOCK_CLOCK_CMD_COMPUTE_DIFF_WITH);
        writer->write_i32((oimf_i32) result);
    };

    oimf_tm_result write_tm(oimf_u8 msg_num, oimf_writer* writer) {
        if (writer->avail_size() < 1) {
            return OIMF_TM_NOT_ENOUGH_SPACE;
        }
        writer->write_u8(msg_num);
        switch (msg_num) {
            case 0:
                return write_current_utc_time(self, writer);
            case 1:
                return write_is_ready(self, writer);
            default:
                return OIMF_TM_INVALID_MESSAGE_NUM;
        }
    };
};
