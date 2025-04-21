package com.example.weaponMaster.api.slack.dto;

import com.example.weaponMaster.modules.slack.dto.UserSlackDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespSlackDto {

    UserSlackDto userSlackInfo;
}
